package org.iimsa.deliveryserver.delivery.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>,
        QuerydslPredicateExecutor<Delivery> {

    Optional<Delivery> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByOrderId(UUID orderId);
}
