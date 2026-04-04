package org.iimsa.deliveryserver.delivery.application.dto.command;

import java.util.List;
import java.util.UUID;

/**
 * Hub Service 이벤트(hub.delivery.create-requested) 기반 배송 생성 커맨드
 * 허브 경로 및 담당자 정보를 포함
 */
public record CreateDeliveryFromHubCommand(

        String correlationId,

        UUID orderId,

        String recipient,

        UUID originHubId,

        String originHubName,

        UUID destinationHubId,

        String destinationHubName,

        List<HubRouteCommand> hubRoutes,

        UUID companyDeliveryManagerId
) {

    public record HubRouteCommand(
            int sequence,
            UUID fromHubId,
            UUID toHubId,
            UUID hubDeliveryManagerId,
            Double estimatedDistance,
            Integer estimatedDuration
    ) {
    }
}
