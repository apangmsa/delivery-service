package org.iimsa.deliveryserver.delivery.application.dto.query;

import java.util.UUID;

public record FindDeliveryQuery(
        UUID deliveryId
) {}
