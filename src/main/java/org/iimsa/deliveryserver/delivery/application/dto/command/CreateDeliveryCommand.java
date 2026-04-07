package org.iimsa.deliveryserver.delivery.application.dto.command;

import java.util.UUID;

public record CreateDeliveryCommand(
        UUID orderId,
        UUID originHubId,
        String originHubName,
        UUID destinationHubId,
        String destinationHubName,
        String recipient
) {}
