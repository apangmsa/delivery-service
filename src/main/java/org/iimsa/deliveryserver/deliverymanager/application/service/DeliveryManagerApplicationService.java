package org.iimsa.deliveryserver.deliverymanager.application.service;

import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.FindDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.ListDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface DeliveryManagerApplicationService {
    DeliveryManagerResult createDeliveryManager(CreateDeliveryManagerCommand command);
    DeliveryManagerResult findDeliveryManager(FindDeliveryManagerQuery query);
    Page<DeliveryManagerResult> listDeliveryManagers(ListDeliveryManagerQuery query);
    DeliveryManagerResult deleteDeliveryManager(UUID deliveryManagerId);
}
