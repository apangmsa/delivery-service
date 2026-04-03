package org.iimsa.deliveryserver.deliverymanager.application.dto.command;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record UpdateDeliveryManagerCommand(
        String username,
        UUID hubId,
        DeliveryManagerType managerType,
        String slackId
) {}
