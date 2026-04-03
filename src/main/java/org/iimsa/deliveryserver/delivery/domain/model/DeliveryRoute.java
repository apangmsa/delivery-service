package org.iimsa.deliveryserver.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.iimsa.common.domain.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Builder
@Access(AccessType.FIELD)
@Table(name = "p_delivery_route", indexes = {
        @Index(name = "idx_delivery_route_delivery_id", columnList = "delivery_id"),
        @Index(name = "idx_delivery_route_sequence", columnList = "delivery_id, sequence")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryRoute extends BaseEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(nullable = false)
    private int sequence; // 경로 순번

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID originHubId;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID destinationHubId;

    private Double estimatedDistance; // 예상 거리 (km)

    private Integer estimatedDuration; // 예상 소요 시간 (분 단위, ex: 90 = 1시간 30분)

    @Enumerated(EnumType.STRING)
    @Column(name = "route_status", length = 20, nullable = false)
    private RouteStatus routeStatus;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    private UUID hubDeliveryManagerId; // 허브 배송 담당자 ID (배정 전 null 가능)

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    public void startMoving(UUID managerId) {
        this.routeStatus = RouteStatus.MOVING;
        this.hubDeliveryManagerId = managerId;
    }

    public void arrive() {
        this.routeStatus = RouteStatus.ARRIVED;
    }

    public void softDelete(String deletedBy) {
        super.delete(deletedBy);
    }
}
