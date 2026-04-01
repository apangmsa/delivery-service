package org.iimsa.deliveryserver.delivery.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iimsa.common.response.CommonResponse;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.application.service.DeliveryApplicationService;
import org.iimsa.deliveryserver.delivery.presentation.dto.request.CreateDeliveryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Delivery", description = "배송 관리 API")
@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryApplicationService deliveryApplicationService;

    @Operation(summary = "배송 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<DeliveryResult> createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request
    ) {
        DeliveryResult result = deliveryApplicationService.createDelivery(request.toCommand());
        return CommonResponse.success("배송이 생성되었습니다.", result);
    }
}
