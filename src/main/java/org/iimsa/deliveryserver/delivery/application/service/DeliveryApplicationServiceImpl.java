package org.iimsa.deliveryserver.delivery.application.service;

import lombok.RequiredArgsConstructor;
import org.iimsa.common.exception.NotFoundException;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Override
    public DeliveryResult findDelivery(FindDeliveryQuery query) {
        Delivery delivery = deliveryRepository.findActiveById(query.deliveryId())
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));
        return DeliveryResult.from(delivery);
    }

    @Override
    public Page<DeliveryResult> listDeliveries(ListDeliveryQuery query) {
        return deliveryRepository.findAllActive(PageRequest.of(query.page(), query.size()))
                .map(DeliveryResult::from);
    }

    @Override
    @Transactional
    public DeliveryResult updateDelivery(UUID deliveryId, UpdateDeliveryCommand command) {
        Delivery delivery = deliveryRepository.findActiveById(deliveryId)
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));

        if (command.deliveryStatus() != null) {
            delivery.updateStatus(command.deliveryStatus());
        }

        // TODO: 업체 배송 담당자 배정 (companyDeliveryManagerId)
        //       user-service에서 Kafka 이벤트로 수신하여 처리!

        return DeliveryResult.from(deliveryRepository.save(delivery));
    }


}
