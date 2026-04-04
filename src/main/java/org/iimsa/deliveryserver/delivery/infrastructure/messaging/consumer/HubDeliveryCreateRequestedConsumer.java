package org.iimsa.deliveryserver.delivery.infrastructure.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.iimsa.common.event.Events;
import org.iimsa.common.messaging.annotation.IdempotentConsumer;
import org.iimsa.deliveryserver.delivery.domain.event.DeliveryCreateFailedPayload;
import org.iimsa.deliveryserver.delivery.domain.event.HubDeliveryCreateRequestedPayload;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.model.RouteStatus;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubDeliveryCreateRequestedConsumer {

    private final DeliveryRepository deliveryRepository;
    private final ObjectMapper objectMapper;

    /**
     * hub.delivery.create-requested 이벤트 소비
     * Hub Service가 배송 경로 및 담당자 정보를 담아 발행하는 이벤트
     *
     * <p>멱등성: @IdempotentConsumer 로 Inbox 패턴 적용 (message_id 헤더 기반 중복 차단)
     * <p>보상 트랜잭션: 배송 생성 실패 시 delivery.create-failed 이벤트 발행
     */
    @Transactional
    @IdempotentConsumer("hub-delivery-create-requested")
    @KafkaListener(
            topics = "${kafka.topics.hub-delivery-create-requested:hub.delivery.create-requested}",
            groupId = "${spring.kafka.consumer.group-id:delivery-service}"
    )
    public void consume(ConsumerRecord<String, String> record) {
        HubDeliveryCreateRequestedPayload payload;

        try {
            payload = objectMapper.readValue(record.value(), HubDeliveryCreateRequestedPayload.class);
        } catch (Exception e) {
            log.error("[hub.delivery.create-requested] 페이로드 역직렬화 실패 - offset={}", record.offset(), e);
            // 역직렬화 실패는 재시도해도 동일하므로 KafkaConfig DefaultErrorHandler → DLT로 격리
            throw new IllegalArgumentException("페이로드 역직렬화 실패", e);
        }

        try {
            createDelivery(payload);
            log.info("[hub.delivery.create-requested] 배송 생성 성공 - orderId={}", payload.orderId());

        } catch (Exception e) {
            log.error("[hub.delivery.create-requested] 배송 생성 실패 - orderId={}, reason={}",
                    payload.orderId(), e.getMessage(), e);

            // 사가 보상 트랜잭션: delivery.create-failed 이벤트 발행
            publishDeliveryCreateFailed(payload, e.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // 배송 생성 (Delivery + DeliveryRoute)
    // ──────────────────────────────────────────────

    private void createDelivery(HubDeliveryCreateRequestedPayload payload) {
        // 중복 주문 검사 (soft delete 이력 포함하여 재등록 차단)
        if (deliveryRepository.existsByOrderId(payload.orderId())) {
            throw new IllegalStateException("이미 배송이 존재하는 주문입니다: " + payload.orderId());
        }

        // Delivery 생성
        Delivery delivery = Delivery.builder()
                .orderId(payload.orderId())
                .deliveryStatus(DeliveryStatus.HUB_WAITING)
                .originHubId(payload.originHubId())
                .originHubName(payload.originHubName())
                .destinationHubId(payload.destinationHubId())
                .destinationHubName(payload.destinationHubName())
                .recipient(payload.recipient())
                .companyDeliveryManagerId(payload.companyDeliveryManagerId())
                .build();

        // DeliveryRoute 생성 (hub route 정보 기반)
        if (payload.hubRoutes() != null) {
            for (HubDeliveryCreateRequestedPayload.HubRouteInfo routeInfo : payload.hubRoutes()) {
                DeliveryRoute route = DeliveryRoute.builder()
                        .delivery(delivery)
                        .sequence(routeInfo.sequence())
                        .originHubId(routeInfo.fromHubId())
                        .destinationHubId(routeInfo.toHubId())
                        .hubDeliveryManagerId(routeInfo.hubDeliveryManagerId())
                        .estimatedDistance(routeInfo.estimatedDistance())
                        .estimatedDuration(routeInfo.estimatedDuration())
                        .routeStatus(RouteStatus.WAITING)
                        .build();

                delivery.getDeliveryRoutes().add(route);
            }
        }

        deliveryRepository.save(delivery);
    }

    // ──────────────────────────────────────────────
    // 보상 트랜잭션: delivery.create-failed 이벤트 발행
    // ──────────────────────────────────────────────

    private void publishDeliveryCreateFailed(HubDeliveryCreateRequestedPayload payload, String reason) {
        String failedCorrelationId = "failed-" + UUID.randomUUID();

        DeliveryCreateFailedPayload failedPayload = new DeliveryCreateFailedPayload(
                payload.correlationId(),
                payload.orderId(),
                reason
        );

        Events.trigger(
                failedCorrelationId,
                "DELIVERY",
                payload.orderId().toString(),
                "delivery.create-failed",
                failedPayload
        );

        log.info("[delivery.create-failed] 보상 이벤트 발행 - correlationId={}, orderId={}",
                failedCorrelationId, payload.orderId());
    }
}
