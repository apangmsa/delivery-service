package org.iimsa.deliveryserver.deliverymanager.presentation.dto.request;

import org.iimsa.deliveryserver.deliverymanager.application.dto.command.UpdateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record UpdateDeliveryManagerRequest(
        UUID hubId,
        DeliveryManagerType managerType,
        String slackId
) {
    public UpdateDeliveryManagerCommand toCommand() {
        return new UpdateDeliveryManagerCommand(hubId, managerType, slackId);
    }
}
