package org.iimsa.deliveryserver.delivery.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.iimsa.deliveryserver.delivery.domain.model.Delivery;
import org.iimsa.deliveryserver.delivery.domain.repository.DeliveryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final JpaDeliveryRepository jpaDeliveryRepository;

    @Override
    public Delivery save(Delivery delivery) {
        return jpaDeliveryRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findActiveById(UUID id) {
        return jpaDeliveryRepository.findActiveById(id);
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return jpaDeliveryRepository.existsByOrderIdAndDeletedAtIsNull(orderId);
    }

    @Override
    public Page<Delivery> findAllActive(Pageable pageable) {
        return jpaDeliveryRepository.findAllActive(pageable);
    }
}
