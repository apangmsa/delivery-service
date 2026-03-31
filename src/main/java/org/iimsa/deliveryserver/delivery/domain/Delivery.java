package org.iimsa.deliveryserver.delivery.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.spartahub.common.domain.BaseUserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@Access(AccessType.FIELD)
@Table(name = "p_delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Delivery extends BaseUserEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column(length = 50)
    private String recipient;

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
