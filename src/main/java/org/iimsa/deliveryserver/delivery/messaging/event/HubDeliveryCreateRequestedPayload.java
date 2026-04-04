package org.iimsa.deliveryserver.delivery.messaging.event;

import java.util.List;
import java.util.UUID;

/**
 * hub.delivery.create-requested 이벤트 페이로드
 * Hub Service → Delivery Service
 * 주문 확정 이후 Hub Service가 배송 경로 및 담당자 정보를 조합해 전달
 */
public record HubDeliveryCreateRequestedPayload(

        /**
         * 메시지 추적용 correlationId
         */
        String correlationId,

        /**
         * 주문 ID
         */
        UUID orderId,

        /**
         * 수령인 정보
         */
        String recipient,

        /**
         * 출발 허브 ID
         */
        UUID originHubId,

        /**
         * 출발 허브 명
         */
        String originHubName,

        /**
         * 도착 허브 ID
         */
        UUID destinationHubId,

        /**
         * 도착 허브 명
         */
        String destinationHubName,

        /**
         * 허브 간 경로 목록 (순서대로)
         */
        List<HubRouteInfo> hubRoutes,

        /**
         * 업체 배송 담당자 ID
         */
        UUID companyDeliveryManagerId
) {

    /**
     * 허브 간 단일 경로 정보
     */
    public record HubRouteInfo(

            /**
             * 경로 순서 (1부터 시작)
             */
            int sequence,

            /**
             * 출발 허브 ID
             */
            UUID fromHubId,

            /**
             * 도착 허브 ID
             */
            UUID toHubId,

            /**
             * 해당 구간 담당 허브 배송 담당자 ID
             */
            UUID hubDeliveryManagerId,

            /**
             * 예상 거리 (km)
             */
            Double estimatedDistance,

            /**
             * 예상 소요 시간 (분)
             */
            Integer estimatedDuration
    ) {
    }
}
