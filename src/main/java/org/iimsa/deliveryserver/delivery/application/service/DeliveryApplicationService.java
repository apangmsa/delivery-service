package org.iimsa.deliveryserver.delivery.application.service;

import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;

public interface DeliveryApplicationService {

    DeliveryResult createDelivery(CreateDeliveryCommand command);
}
