package org.iimsa.deliveryserver.delivery.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.UUID;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>,
        QuerydslPredicateExecutor<DeliveryRoute> {

    List<DeliveryRoute> findByDeliveryIdAndDeletedAtIsNullOrderBySequenceAsc(UUID deliveryId);
}
