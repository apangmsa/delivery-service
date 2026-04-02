package org.iimsa.deliveryserver.deliverymanager.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iimsa.common.response.CommonResponse;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;
import org.iimsa.deliveryserver.deliverymanager.application.service.DeliveryManagerApplicationService;
import org.iimsa.deliveryserver.deliverymanager.presentation.dto.request.CreateDeliveryManagerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DeliveryManager", description = "배송 담당자 관리 API")
@RestController
@RequestMapping("/api/v1/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerApplicationService deliveryManagerApplicationService;

    @Operation(summary = "배송 담당자 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<DeliveryManagerResult> createDeliveryManager(
            @Valid @RequestBody CreateDeliveryManagerRequest request
    ) {
        DeliveryManagerResult result = deliveryManagerApplicationService.createDeliveryManager(request.toCommand());
        return CommonResponse.success("배송 담당자가 등록되었습니다.", result);
    }
}
