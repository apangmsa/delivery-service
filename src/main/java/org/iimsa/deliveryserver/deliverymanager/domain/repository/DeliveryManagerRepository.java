package org.iimsa.deliveryserver.deliverymanager.domain.repository;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {
    DeliveryManager save(DeliveryManager deliveryManager);
    Optional<DeliveryManager> findActiveById(UUID id);
    boolean existsByUserId(UUID userId);
}
