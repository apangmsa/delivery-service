package org.iimsa.deliveryserver.delivery.domain.repository;

import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);

    Optional<Delivery> findActiveById(UUID id);

    boolean existsByOrderId(UUID orderId);

    Page<Delivery> findAllActive(Pageable pageable);
}
