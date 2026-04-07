package org.iimsa.deliveryserver.deliverymanager.application.dto.query;

public record ListDeliveryManagerQuery(int page, int size) {
    public ListDeliveryManagerQuery {
        if (page < 0) throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
        if (size < 1) throw new IllegalArgumentException("size는 1 이상이어야 합니다.");
    }
}
