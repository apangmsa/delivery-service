package org.iimsa.deliveryserver.delivery.infrastructure.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.iimsa.common.event.Events;
import org.iimsa.common.messaging.annotation.IdempotentConsumer;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryFromHubCommand;
import org.iimsa.deliveryserver.delivery.application.service.DeliveryApplicationService;
import org.iimsa.deliveryserver.delivery.domain.event.DeliveryCreateFailedPayload;
import org.iimsa.deliveryserver.delivery.domain.event.HubDeliveryCreateRequestedPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubDeliveryCreateRequestedConsumer {

    private final DeliveryApplicationService deliveryApplicationService;
    private final ObjectMapper objectMapper;

    /**
     * hub.delivery.create-requested 이벤트 소비
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
            throw new IllegalArgumentException("페이로드 역직렬화 실패", e);
        }

        try {
            deliveryApplicationService.createDeliveryFromHub(toCommand(payload));
            log.info("[hub.delivery.create-requested] 배송 생성 성공 - orderId={}", payload.orderId());

        } catch (Exception e) {
            log.error("[hub.delivery.create-requested] 배송 생성 실패 - orderId={}, reason={}",
                    payload.orderId(), e.getMessage(), e);
            publishDeliveryCreateFailed(payload, e.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // Payload → Command 변환
    // ──────────────────────────────────────────────

    private CreateDeliveryFromHubCommand toCommand(HubDeliveryCreateRequestedPayload payload) {
        List<CreateDeliveryFromHubCommand.HubRouteCommand> routeCommands =
                payload.hubRoutes() == null ? List.of() :
                payload.hubRoutes().stream()
                        .map(r -> new CreateDeliveryFromHubCommand.HubRouteCommand(
                                r.sequence(),
                                r.fromHubId(),
                                r.toHubId(),
                                r.hubDeliveryManagerId(),
                                r.estimatedDistance(),
                                r.estimatedDuration()
                        ))
                        .toList();

        return new CreateDeliveryFromHubCommand(
                payload.correlationId(),
                payload.orderId(),
                payload.recipient(),
                payload.originHubId(),
                payload.originHubName(),
                payload.destinationHubId(),
                payload.destinationHubName(),
                routeCommands,
                payload.companyDeliveryManagerId()
        );
    }

    // ──────────────────────────────────────────────
    // 보상 트랜잭션: delivery.create-failed 이벤트 발행
    // ──────────────────────────────────────────────

    private void publishDeliveryCreateFailed(HubDeliveryCreateRequestedPayload payload, String reason) {
        String failedCorrelationId = "failed-" + UUID.randomUUID();

        Events.trigger(
                failedCorrelationId,
                "DELIVERY",
                payload.orderId().toString(),
                "delivery.create-failed",
                new DeliveryCreateFailedPayload(payload.correlationId(), payload.orderId(), reason)
        );

        log.info("[delivery.create-failed] 보상 이벤트 발행 - correlationId={}, orderId={}",
                failedCorrelationId, payload.orderId());
    }
}
