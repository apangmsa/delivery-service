package org.iimsa.deliveryserver.delivery.application.service;

import lombok.RequiredArgsConstructor;
import org.iimsa.common.exception.ConflictException;
import org.iimsa.common.exception.NotFoundException;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.command.CreateDeliveryFromHubCommand;
import org.iimsa.deliveryserver.delivery.application.dto.command.UpdateDeliveryCommand;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;
import org.iimsa.deliveryserver.delivery.domain.model.RouteStatus;
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
        if (deliveryRepository.existsByOrderId(command.orderId())) {
            throw new ConflictException("이미 해당 주문에 대한 배송이 존재합니다.");
        }

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
    @Transactional
    public DeliveryResult createDeliveryFromHub(CreateDeliveryFromHubCommand command) {
        if (deliveryRepository.existsByOrderId(command.orderId())) {
            throw new ConflictException("이미 해당 주문에 대한 배송이 존재합니다.");
        }

        Delivery delivery = Delivery.builder()
                .orderId(command.orderId())
                .deliveryStatus(DeliveryStatus.HUB_WAITING)
                .originHubId(command.originHubId())
                .originHubName(command.originHubName())
                .destinationHubId(command.destinationHubId())
                .destinationHubName(command.destinationHubName())
                .recipient(command.recipient())
                .companyDeliveryManagerId(command.companyDeliveryManagerId())
                .build();

        if (command.hubRoutes() != null) {
            for (CreateDeliveryFromHubCommand.HubRouteCommand routeCommand : command.hubRoutes()) {
                DeliveryRoute route = DeliveryRoute.builder()
                        .delivery(delivery)
                        .sequence(routeCommand.sequence())
                        .originHubId(routeCommand.fromHubId())
                        .destinationHubId(routeCommand.toHubId())
                        .hubDeliveryManagerId(routeCommand.hubDeliveryManagerId())
                        .estimatedDistance(routeCommand.estimatedDistance())
                        .estimatedDuration(routeCommand.estimatedDuration())
                        .routeStatus(RouteStatus.WAITING)
                        .build();

                delivery.getDeliveryRoutes().add(route);
            }
        }

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

    @Override
    @Transactional
    public DeliveryResult deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findActiveById(deliveryId)
                .orElseThrow(() -> new NotFoundException("배송 정보를 찾을 수 없습니다."));
        delivery.softDelete(null); // SecurityUtil.getCurrentUsername() 으로 자동 처리 (BaseEntity)
        return DeliveryResult.from(deliveryRepository.save(delivery));
    }
}
