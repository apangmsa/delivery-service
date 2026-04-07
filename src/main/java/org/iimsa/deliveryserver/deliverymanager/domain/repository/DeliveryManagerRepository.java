package org.iimsa.deliveryserver.deliverymanager.domain.repository;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {
    DeliveryManager save(DeliveryManager deliveryManager);
    Optional<DeliveryManager> findActiveById(UUID id);
    Page<DeliveryManager> findAllActive(Pageable pageable);
    boolean existsByUserId(UUID userId);
}
