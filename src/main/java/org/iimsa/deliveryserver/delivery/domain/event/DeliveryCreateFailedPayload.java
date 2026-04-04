package org.iimsa.deliveryserver.delivery.domain.event;

import java.util.UUID;

/**
 * delivery.create-failed 이벤트 페이로드
 * Delivery Service → Order Service / Hub Service
 * 배송 생성 실패 시 발행하는 사가 보상 트랜잭션 이벤트
 */
public record DeliveryCreateFailedPayload(

        /**
         * 메시지 추적용 correlationId (hub.delivery.create-requested 의 correlationId 와 동일)
         */
        String correlationId,

        /**
         * 실패한 주문 ID
         */
        UUID orderId,

        /**
         * 실패 사유
         */
        String reason
) {
}
