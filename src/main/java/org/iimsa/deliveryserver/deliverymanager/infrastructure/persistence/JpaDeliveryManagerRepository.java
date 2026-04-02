package org.iimsa.deliveryserver.deliverymanager.infrastructure.persistence;

import org.iimsa.deliveryserver.deliverymanager.domain.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>,
        QuerydslPredicateExecutor<DeliveryManager> {

    @Query("SELECT m FROM DeliveryManager m WHERE m.id = :id AND m.deletedAt IS NULL")
    Optional<DeliveryManager> findActiveById(UUID id);

    boolean existsByUserIdAndDeletedAtIsNull(UUID userId);
}
