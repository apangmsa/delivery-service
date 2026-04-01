package org.iimsa.deliveryserver.delivery.application.dto.command;

import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;

public record UpdateDeliveryCommand(
        DeliveryStatus deliveryStatus
) {}
