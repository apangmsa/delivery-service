package org.iimsa.deliveryserver.deliverymanager.application.dto.command;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManagerType;

import java.util.UUID;

public record CreateDeliveryManagerCommand(
        UUID userId,
        String username,
        UUID hubId,
        DeliveryManagerType managerType,
        String slackId
) {}
