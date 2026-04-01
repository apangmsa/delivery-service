package org.iimsa.deliveryserver.delivery.application.service;

import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.springframework.data.domain.Page;

public interface DeliveryApplicationService {

    DeliveryResult createDelivery(CreateDeliveryCommand command);

    DeliveryResult findDelivery(FindDeliveryQuery query);

    Page<DeliveryResult> listDeliveries(ListDeliveryQuery query);
}
