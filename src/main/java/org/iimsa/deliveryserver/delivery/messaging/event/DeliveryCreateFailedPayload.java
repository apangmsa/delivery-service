package org.iimsa.deliveryserver.delivery.messaging.event;

import java.util.UUID;

/**
 * delivery.create-failed 이벤트 페이로드
 * Hub Service → Delivery Service 에서 배송 생성 실패 시 발행
 * → Order Service / Hub Service 에서 사가 보상 트랜잭션 수행
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
