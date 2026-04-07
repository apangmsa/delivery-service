package org.iimsa.deliveryserver.delivery.infrastructure.persistence;

import org.iimsa.deliveryserver.delivery.domain.model.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID>,
        QuerydslPredicateExecutor<DeliveryRoute> {

    @Query("SELECT r FROM DeliveryRoute r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<DeliveryRoute> findActiveById(@Param("id") UUID id);

    @Query("""
            SELECT r FROM DeliveryRoute r
            WHERE r.delivery.id = :deliveryId AND r.deletedAt IS NULL
            ORDER BY r.sequence ASC
            """)
    List<DeliveryRoute> findActiveByDeliveryId(@Param("deliveryId") UUID deliveryId);
}
