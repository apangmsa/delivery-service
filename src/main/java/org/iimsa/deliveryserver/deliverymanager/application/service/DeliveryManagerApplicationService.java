package org.iimsa.deliveryserver.deliverymanager.application.service;

import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;

public interface DeliveryManagerApplicationService {
    DeliveryManagerResult createDeliveryManager(CreateDeliveryManagerCommand command);
}
