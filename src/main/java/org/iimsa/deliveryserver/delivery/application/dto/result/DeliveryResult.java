package org.iimsa.deliveryserver.delivery.application.dto.result;

import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.model.RouteStatus;

import java.util.List;
import java.util.UUID;

public record DeliveryResult(
        UUID id,
        UUID orderId,
        DeliveryStatus deliveryStatus,
        UUID originHubId,
        String originHubName,
        UUID destinationHubId,
        String destinationHubName,
        String recipient,
        UUID companyDeliveryManagerId,
        List<DeliveryRouteResult> deliveryRoutes
) {
    public static DeliveryResult from(Delivery delivery) {
        return new DeliveryResult(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDeliveryStatus(),
                delivery.getOriginHubId(),
                delivery.getOriginHubName(),
                delivery.getDestinationHubId(),
                delivery.getDestinationHubName(),
                delivery.getRecipient(),
                delivery.getCompanyDeliveryManagerId(),
                delivery.getDeliveryRoutes().stream()
                        .map(DeliveryRouteResult::from)
                        .toList()
        );
    }

    public record DeliveryRouteResult(
            UUID id,
            int sequence,
            UUID originHubId,
            UUID destinationHubId,
            Double estimatedDistance,
            Integer estimatedDuration,
            RouteStatus routeStatus,
            UUID hubDeliveryManagerId
    ) {
        public static DeliveryRouteResult from(DeliveryRoute route) {
            return new DeliveryRouteResult(
                    route.getId(),
                    route.getSequence(),
                    route.getOriginHubId(),
                    route.getDestinationHubId(),
                    route.getEstimatedDistance(),
                    route.getEstimatedDuration(),
                    route.getRouteStatus(),
                    route.getHubDeliveryManagerId()
            );
        }
    }
}
