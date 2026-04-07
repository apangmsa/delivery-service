package org.iimsa.deliveryserver.delivery.domain.repository;

import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {

    DeliveryRoute save(DeliveryRoute deliveryRoute);

    Optional<DeliveryRoute> findActiveById(UUID id);

    /** 배송 ID 기준 활성 경로 목록 — sequence 오름차순 */
    List<DeliveryRoute> findActiveByDeliveryId(UUID deliveryId);
}
