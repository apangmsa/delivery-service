package org.iimsa.deliveryserver.deliverymanager.application.service;

import lombok.RequiredArgsConstructor;
import org.iimsa.common.exception.ConflictException;
import org.iimsa.common.exception.NotFoundException;
import org.iimsa.deliveryserver.deliverymanager.application.dto.command.CreateDeliveryManagerCommand;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.FindDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.ListDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.iimsa.deliveryserver.deliverymanager.domain.repository.DeliveryManagerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public DeliveryManagerResult findDeliveryManager(FindDeliveryManagerQuery query) {
        DeliveryManager manager = deliveryManagerRepository.findActiveById(query.deliveryManagerId())
                .orElseThrow(() -> new NotFoundException("배송 담당자를 찾을 수 없습니다."));
        return DeliveryManagerResult.from(manager);
    }

    @Override
    public Page<DeliveryManagerResult> listDeliveryManagers(ListDeliveryManagerQuery query) {
        return deliveryManagerRepository.findAllActive(PageRequest.of(query.page(), query.size()))
                .map(DeliveryManagerResult::from);
    }
}
