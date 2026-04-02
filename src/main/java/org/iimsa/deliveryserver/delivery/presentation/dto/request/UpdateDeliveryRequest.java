package org.iimsa.deliveryserver.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;

public record UpdateDeliveryRequest(
        @NotNull (message = "배송 상태는 필수입니다.")
        DeliveryStatus deliveryStatus
) {
    public UpdateDeliveryCommand toCommand() {
        return new UpdateDeliveryCommand(deliveryStatus);
    }
}
