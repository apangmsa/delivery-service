package org.iimsa.deliveryserver.deliverymanager.application.service;

import lombok.RequiredArgsConstructor;
import org.iimsa.common.exception.ConflictException;
import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.iimsa.deliveryserver.deliverymanager.domain.repository.DeliveryManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryManagerApplicationServiceImpl implements DeliveryManagerApplicationService {

    private final DeliveryManagerRepository deliveryManagerRepository;

    @Override
    @Transactional
    public DeliveryManagerResult createDeliveryManager(CreateDeliveryManagerCommand command) {
        if (deliveryManagerRepository.existsByUserId(command.userId())) {
            throw new ConflictException("이미 등록된 배송 담당자입니다.");
        }

        DeliveryManager manager = DeliveryManager.builder()
                .userId(command.userId())
                .hubId(command.hubId())
                .managerType(command.managerType())
                .slackId(command.slackId())
                .build();

        return DeliveryManagerResult.from(deliveryManagerRepository.save(manager));
    }
}
