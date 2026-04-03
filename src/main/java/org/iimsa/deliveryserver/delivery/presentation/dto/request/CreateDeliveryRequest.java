package org.iimsa.deliveryserver.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;

import java.util.UUID;

public record CreateDeliveryRequest(

        @NotNull(message = "주문 ID는 필수입니다.")
        UUID orderId,

        @NotNull(message = "출발 허브 ID는 필수입니다.")
        UUID originHubId,

        @NotBlank(message = "출발 허브명은 필수입니다.")
        String originHubName,

        @NotNull(message = "도착 허브 ID는 필수입니다.")
        UUID destinationHubId,

        @NotBlank(message = "도착 허브명은 필수입니다.")
        String destinationHubName,

        @NotBlank(message = "수령인은 필수입니다.")
        String recipient
) {
    public CreateDeliveryCommand toCommand() {
        return new CreateDeliveryCommand(
                orderId,
                originHubId,
                originHubName,
                destinationHubId,
                destinationHubName,
                recipient
        );
    }
}
