package org.iimsa.deliveryserver.deliverymanager.application.dto.result;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryManagerResult(
        UUID id,
        UUID userId,
        UUID hubId,
        DeliveryManagerType managerType,
        String slackId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static DeliveryManagerResult from(DeliveryManager manager) {
        return new DeliveryManagerResult(
                manager.getId(),
                manager.getUserId(),
                manager.getHubId(),
                manager.getManagerType(),
                manager.getSlackId(),
                manager.getCreatedAt(),
                manager.getModifiedAt()
        );
    }
}
