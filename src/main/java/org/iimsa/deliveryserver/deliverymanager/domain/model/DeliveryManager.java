package org.iimsa.deliveryserver.deliverymanager.domain.model;

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
@Table(
        name = "p_delivery_manager",
        uniqueConstraints = @UniqueConstraint(name = "uq_delivery_manager_user_id", columnNames = "user_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryManager extends BaseEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "user_id", length = 36, nullable = false)
    private UUID userId;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(length = 36, nullable = false)
    private UUID hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "manager_type", length = 20, nullable = false)
    private DeliveryManagerType managerType;

    @Column(length = 100)
    private String slackId;

    // ──────────────────────────────────────────────
    // 비즈니스 메서드
    // ──────────────────────────────────────────────

    public void softDelete(String deletedBy) {
        super.delete(deletedBy);
    }
}
