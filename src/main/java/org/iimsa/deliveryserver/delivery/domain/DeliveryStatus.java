package org.iimsa.deliveryserver.delivery.domain;

public enum DeliveryStatus {
    HUB_WAITING,       // 허브 대기
    HUB_MOVING,        // 허브 이동 중
    HUB_ARRIVED,       // 목적지 허브 도착
    COMPANY_MOVING,    // 업체 배송 중
    COMPANY_DELIVERED  // 업체 배송 완료
}
