package org.iimsa.deliveryserver.deliverymanager.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record CreateDeliveryManagerRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        UUID userId,

        @NotNull(message = "허브 ID는 필수입니다.")
        UUID hubId,

        @NotNull(message = "담당자 유형은 필수입니다.")
        DeliveryManagerType managerType,

        String slackId
) {
    public CreateDeliveryManagerCommand toCommand() {
        return new CreateDeliveryManagerCommand(userId, hubId, managerType, slackId);
    }
}
