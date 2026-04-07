package org.iimsa.deliveryserver.delivery.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iimsa.common.event.Events;
import org.iimsa.common.exception.ConflictException;
import org.iimsa.common.exception.NotFoundException;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryFromHubCommand;
import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.domain.event.DeliveryStatusChangedPayload;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.model.RouteStatus;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryApplicationServiceImpl implements DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    @Value("${kafka.topics.delivery-status-changed:delivery.status-changed}")
    private String deliveryStatusChangedTopic;

    @Override
    @Transactional
    public DeliveryResult createDeliveryFromHub(CreateDeliveryFromHubCommand command) {
        if (deliveryRepository.existsByOrderId(command.orderId())) {
            throw new ConflictException("이미 해당 주문에 대한 배송이 존재합니다.");
        }

        // Hub Service가 미리 생성한 deliveryId 사용
        Delivery delivery = Delivery.builder()
                .id(command.deliveryId())
                .orderId(command.orderId())
                .deliveryStatus(DeliveryStatus.HUB_WAITING)
                .originHubId(command.originHubId())
                .originHubName(command.originHubName())
                .destinationHubId(command.destinationHubId())
                .destinationHubName(command.destinationHubName())
                .receiverId(command.receiverId())
                .receiverName(command.receiverName())
                .build();

        if (command.routeSegments() != null) {
            for (CreateDeliveryFromHubCommand.RouteSegmentCommand seg : command.routeSegments()) {
                DeliveryRoute route = DeliveryRoute.builder()
                        .delivery(delivery)
                        .sequence(seg.sequence())
                        .originHubId(seg.fromHubId())
                        .destinationHubId(seg.toHubId())
                        .estimatedDuration(seg.estimatedDuration())
                        .estimatedDistance(seg.estimatedDistance())
                        .routeStatus(RouteStatus.WAITING)
                        .build();

                delivery.getDeliveryRoutes().add(route);
            }
        }

        return DeliveryResult.from(deliveryRepository.save(delivery));
    }

    @Override
    public DeliveryResult findDelivery(FindDeliveryQuery query) {
        Delivery delivery = deliveryRepository.findActiveById(query.deliveryId())
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));
        return DeliveryResult.from(delivery);
    }

    @Override
    public Page<DeliveryResult> listDeliveries(ListDeliveryQuery query) {
        return deliveryRepository.findAllActive(PageRequest.of(query.page(), query.size()))
                .map(DeliveryResult::from);
    }

    @Override
    @Transactional
    public DeliveryResult updateDelivery(UUID deliveryId, UpdateDeliveryCommand command) {
        Delivery delivery = deliveryRepository.findActiveById(deliveryId)
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));

        DeliveryStatus previousStatus = delivery.getDeliveryStatus();

        if (command.deliveryStatus() != null) {
            delivery.updateStatus(command.deliveryStatus());
        }
        DeliveryResult result = DeliveryResult.from(deliveryRepository.save(delivery));

        // 상태가 실제로 변경된 경우에만 ai-service 로 이벤트 발행
        if (command.deliveryStatus() != null && !command.deliveryStatus().equals(previousStatus)) {
            publishStatusChangedEvent(delivery, previousStatus);
        }

        return result;
    }

    @Override
    @Transactional
    public DeliveryResult deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findActiveById(deliveryId)
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));
        delivery.softDelete(null);
        return DeliveryResult.from(deliveryRepository.save(delivery));
    }

    // ──────────────────────────────────────────────
    // private helpers
    // ──────────────────────────────────────────────

    /**
     * 배송 상태 변경 이벤트 발행 → ai-service (Outbox 패턴)
     *
     * <p>토픽: {@code delivery.status-changed} (application.yml {@code kafka.topics.delivery-status-changed})
     */
    private void publishStatusChangedEvent(Delivery delivery, DeliveryStatus previousStatus) {
        String correlationId = UUID.randomUUID().toString();

        Events.trigger(
                correlationId,
                "DELIVERY",
                delivery.getId().toString(),
                deliveryStatusChangedTopic,
                new DeliveryStatusChangedPayload(
                        delivery.getId(),
                        delivery.getOrderId(),
                        previousStatus,
                        delivery.getDeliveryStatus(),
                        LocalDateTime.now()
                )
        );

        log.info("[delivery.status-changed] 이벤트 발행 - deliveryId={}, {} → {}",
                delivery.getId(), previousStatus, delivery.getDeliveryStatus());
    }
}
