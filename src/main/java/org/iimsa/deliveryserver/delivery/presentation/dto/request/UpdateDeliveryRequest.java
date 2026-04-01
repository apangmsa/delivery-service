package org.iimsa.deliveryserver.delivery.presentation.dto.request;

import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;

public record UpdateDeliveryRequest(
        DeliveryStatus deliveryStatus
) {
    public UpdateDeliveryCommand toCommand() {
        return new UpdateDeliveryCommand(deliveryStatus);
    }
}
