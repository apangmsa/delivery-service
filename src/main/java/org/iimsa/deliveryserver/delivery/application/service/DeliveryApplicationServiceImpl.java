package org.iimsa.deliveryserver.delivery.application.service;

import lombok.RequiredArgsConstructor;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryApplicationServiceImpl implements DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public DeliveryResult createDelivery(CreateDeliveryCommand command) {
        Delivery delivery = Delivery.builder()
                .orderId(command.orderId())
                .deliveryStatus(DeliveryStatus.HUB_WAITING)
                .originHubId(command.originHubId())
                .originHubName(command.originHubName())
                .destinationHubId(command.destinationHubId())
                .destinationHubName(command.destinationHubName())
                .recipient(command.recipient())
                .build();

        return DeliveryResult.from(deliveryRepository.save(delivery));
    }
}
