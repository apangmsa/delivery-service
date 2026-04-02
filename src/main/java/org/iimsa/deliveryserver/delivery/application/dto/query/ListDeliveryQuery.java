package org.iimsa.deliveryserver.delivery.application.dto.query;

public record ListDeliveryQuery(
        int page,
        int size
) {
    public ListDeliveryQuery {
       if (page < 0) {
           throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
       }
       if (size < 1) {
           throw new IllegalArgumentException("size는 1 이상이어야 합니다.");
       }
    }
}
