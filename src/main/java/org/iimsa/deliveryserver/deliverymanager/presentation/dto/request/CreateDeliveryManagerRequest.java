package org.iimsa.deliveryserver.deliverymanager.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record CreateDeliveryManagerRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        UUID userId,

        @NotBlank(message = "담당자 이름은 필수입니다.")
        @Size(max = 100, message = "담당자 이름은 100자 이하여야 합니다.")
        String username,

        @NotNull(message = "허브 ID는 필수입니다.")
        UUID hubId,

        @NotNull(message = "담당자 유형은 필수입니다.")
        DeliveryManagerType managerType,

        @Size(max = 100, message = "SlackID는 100자 이하여야 합니다.")
        String slackId
) {
    public CreateDeliveryManagerCommand toCommand() {
        return new CreateDeliveryManagerCommand(userId, username, hubId, managerType, slackId);
    }
}
