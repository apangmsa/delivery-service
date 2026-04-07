package org.iimsa.deliveryserver.delivery.application.service;

import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryFromHubCommand;
import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface DeliveryApplicationService {

    /** Hub Service 이벤트(hub.delivery.create-requested) 기반 배송 생성 (경로 포함) */
    DeliveryResult createDeliveryFromHub(CreateDeliveryFromHubCommand command);

    DeliveryResult findDelivery(FindDeliveryQuery query);

    Page<DeliveryResult> listDeliveries(ListDeliveryQuery query);

    DeliveryResult updateDelivery(UUID deliveryId, UpdateDeliveryCommand command);

    DeliveryResult deleteDelivery(UUID deliveryId);
}
