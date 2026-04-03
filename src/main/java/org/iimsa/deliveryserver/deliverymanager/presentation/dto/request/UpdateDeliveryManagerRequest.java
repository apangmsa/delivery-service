package org.iimsa.deliveryserver.deliverymanager.presentation.dto.request;

import jakarta.validation.constraints.Size;
import org.iimsa.deliveryserver.deliverymanager.application.dto.command.UpdateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record UpdateDeliveryManagerRequest(
        @Size(max = 100, message = "담당자 이름은 100자 이하여야 합니다.")
        String username,
        UUID hubId,
        DeliveryManagerType managerType,
        @Size(max = 100, message = "Slack ID는 100자 이하여야 합니다.")
        String slackId
) {
    public UpdateDeliveryManagerCommand toCommand() {
        return new UpdateDeliveryManagerCommand(username, hubId, managerType, slackId);
    }
}
