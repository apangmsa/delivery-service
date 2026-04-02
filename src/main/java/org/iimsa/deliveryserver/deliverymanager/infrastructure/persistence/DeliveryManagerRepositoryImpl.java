package org.iimsa.deliveryserver.deliverymanager.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.iimsa.deliveryserver.deliverymanager.domain.repository.DeliveryManagerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

    private final JpaDeliveryManagerRepository jpaDeliveryManagerRepository;

    @Override
    public DeliveryManager save(DeliveryManager deliveryManager) {
        return jpaDeliveryManagerRepository.save(deliveryManager);
    }

    @Override
    public Optional<DeliveryManager> findActiveById(UUID id) {
        return jpaDeliveryManagerRepository.findActiveById(id);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaDeliveryManagerRepository.existsByUserIdAndDeletedAtIsNull(userId);
    }
}
