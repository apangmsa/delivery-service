package org.iimsa.deliveryserver.delivery.domain.event;

import java.util.List;
import java.util.UUID;

/**
 * hub.delivery.create-requested 이벤트 페이로드
 * Hub Service → Delivery Service
 *
 * <p>Hub Service 가 주문 확정 후 최적 경로를 산출하고,
 * 미리 생성한 {@code deliveryId} 와 함께 배송 생성을 요청합니다.
 */
public record HubDeliveryCreateRequestedPayload(

        /** 메시지 추적용 correlationId */
        String correlationId,

        /** 주문 ID */
        UUID orderId,

        /** Hub 서비스가 미리 생성한 배송 ID (Delivery 엔티티 ID로 사용) */
        UUID deliveryId,

        // ── 상품 정보
        UUID productId,
        String productName,
        Integer productQuantity,

        // ── 공급 업체 (출발지)
        UUID supplierId,
        String supplierName,

        /** 출발 허브 ID */
        UUID startHubId,
        /** 출발 허브명 */
        String startHubName,

        // ── 수령인 (도착지)
        UUID receiverId,
        String receiverName,

        /** 도착 허브 ID */
        UUID endHubId,
        /** 도착 허브명 */
        String endHubName,

        /** 전체 예상 소요 시간 합산 (분) */
        Integer totalEstimatedDuration,

        /** 전체 예상 거리 합산 (km) */
        Double totalEstimatedDistance,

        /** 허브 간 최적 경로 전체 */
        RoutePathData routePath,

        /** 주문 요청 사항 */
        String requestDetails

) {

    /**
     * 허브 간 전체 최적 경로 데이터
     * HubRoute 서비스의 HubRoutePath 를 Kafka 메시지로 직렬화한 구조
     */
    public record RoutePathData(
            UUID originHubId,
            UUID destinationHubId,
            int totalDuration,
            double totalDistance,
            List<SegmentData> segments
    ) {
    }

    /**
     * 단일 허브 구간 정보
     */
    public record SegmentData(
            int sequence,
            UUID fromHubId,
            String fromHubName,
            UUID toHubId,
            String toHubName,
            Integer estimatedDuration,
            Double estimatedDistance
    ) {
    }
}
