package org.iimsa.deliveryserver.delivery.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.iimsa.common.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// GenerationType import 불필요 — @GeneratedValue 미사용

@Entity
@Getter
@Builder
@Access(AccessType.FIELD)
@Table(
        name = "p_delivery",
        uniqueConstraints = @UniqueConstraint(name = "uq_delivery_order_id", columnNames = "order_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Delivery extends BaseEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    private UUID id;  // Hub Service 제공 deliveryId 사용 — @GeneratedValue 미사용

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 30, nullable = false)
    private DeliveryStatus deliveryStatus;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID originHubId;

    @Column(length = 50)
    private String originHubName;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID destinationHubId;

    @Column(length = 50)
    private String destinationHubName;

    // ── 수령인 정보 (receiverId: User 서비스 ID, receiverName: 이름)
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    private UUID receiverId;

    @Column(length = 100)
    private String receiverName;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    private UUID companyDeliveryManagerId; // 업체 배송 담당자 ID (배정 전 null 가능)

    @Builder.Default
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    public void updateStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void assignCompanyDeliveryManager(UUID managerId) {
        this.companyDeliveryManagerId = managerId;
    }

    public void softDelete(String deletedBy) {
        super.delete(deletedBy);
    }
}
