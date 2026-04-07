package org.iimsa.deliveryserver.delivery.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRouteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

    private final JpaDeliveryRouteRepository jpaDeliveryRouteRepository;

    @Override
    public DeliveryRoute save(DeliveryRoute deliveryRoute) {
        return jpaDeliveryRouteRepository.save(deliveryRoute);
    }

    @Override
    public Optional<DeliveryRoute> findActiveById(UUID id) {
        return jpaDeliveryRouteRepository.findActiveById(id);
    }

    @Override
    public List<DeliveryRoute> findActiveByDeliveryId(UUID deliveryId) {
        return jpaDeliveryRouteRepository.findActiveByDeliveryId(deliveryId);
    }
}
