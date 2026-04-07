package org.iimsa.deliveryserver.delivery.application.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * Hub Service 이벤트(hub.delivery.create-requested) 기반 배송 생성 커맨드
 *
 * <p>{@code deliveryId} 는 Hub Service 가 미리 생성하여 페이로드에 포함시킵니다.
 * Delivery 엔티티는 이 ID를 그대로 사용합니다.
 */
public record CreateDeliveryFromHubCommand(

        String correlationId,

        UUID orderId,

        /** Hub Service가 미리 생성한 배송 ID */
        UUID deliveryId,

        UUID originHubId,
        String originHubName,

        UUID destinationHubId,
        String destinationHubName,

        UUID receiverId,
        String receiverName,

        List<RouteSegmentCommand> routeSegments

) {

    /**
     * 허브 간 단일 구간 커맨드
     * (hubDeliveryManagerId 는 배정 전이므로 null)
     */
    public record RouteSegmentCommand(
            int sequence,
            UUID fromHubId,
            UUID toHubId,
            Integer estimatedDuration,
            Double estimatedDistance
    ) {
    }
}
