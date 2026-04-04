package org.iimsa.deliveryserver.delivery.domain.event;

import org.iimsa.deliveryserver.delivery.domain.model.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * delivery.status-changed 이벤트 페이로드
 * Delivery Service → Order Service / Notification Service
 *
 * <p>Order Service는 COMPANY_DELIVERED 상태만 수신하여 주문을 COMPLETED 로 전환
 * <p>Notification Service는 모든 상태 변경을 수신하여 슬랙 알림 발송
 */
public record DeliveryStatusChangedPayload(

        /**
         * 배송 ID
         */
        UUID deliveryId,

        /**
         * 주문 ID (Order Service 연계용)
         */
        UUID orderId,

        /**
         * 변경 전 상태
         */
        DeliveryStatus previousStatus,

        /**
         * 변경 후 상태
         */
        DeliveryStatus currentStatus,

        /**
         * 상태 변경 시각
         */
        LocalDateTime changedAt
) {
}
