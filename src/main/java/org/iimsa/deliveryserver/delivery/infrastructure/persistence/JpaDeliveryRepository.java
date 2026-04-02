package org.iimsa.deliveryserver.delivery.infrastructure.persistence;

import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID>,
        QuerydslPredicateExecutor<Delivery> {

    @Query("SELECT d FROM Delivery d WHERE d.id = :id AND d.deletedAt IS NULL")
    Optional<Delivery> findActiveById(UUID id);

    @Query("""
        SELECT d 
                FROM Delivery d 
                        WHERE d.deletedAt IS NULL 
                                ORDER BY d.createdAt DESC, d.id DESC
                                        """)
    Page<Delivery> findAllActive(Pageable pageable);

    boolean existsByOrderIdAndDeletedAtIsNull(UUID orderId);
}
