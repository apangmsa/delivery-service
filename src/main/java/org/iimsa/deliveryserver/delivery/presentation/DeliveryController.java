package org.iimsa.deliveryserver.delivery.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iimsa.common.response.CommonResponse;
import org.iimsa.deliveryserver.delivery.application.dto.query.FindDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.query.ListDeliveryQuery;
import org.iimsa.deliveryserver.delivery.application.dto.result.DeliveryResult;
import org.iimsa.deliveryserver.delivery.application.service.DeliveryApplicationService;
import org.iimsa.deliveryserver.delivery.presentation.dto.request.CreateDeliveryRequest;
import org.iimsa.deliveryserver.delivery.presentation.dto.request.UpdateDeliveryRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(summary = "배송 단건 조회")
    @GetMapping("/{deliveryId}")
    public CommonResponse<DeliveryResult> findDelivery(
            @PathVariable UUID deliveryId
    ) {
        DeliveryResult result = deliveryApplicationService.findDelivery(new FindDeliveryQuery(deliveryId));
        return CommonResponse.success(result);
    }

    @Operation(summary = "배송 목록 조회")
    @GetMapping
    public CommonResponse<Page<DeliveryResult>> listDeliveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DeliveryResult> result = deliveryApplicationService.listDeliveries(new ListDeliveryQuery(page, size));
        return CommonResponse.success(result);
    }

    @Operation(summary = "배송 상태 수정")
    @PatchMapping("/{deliveryId}")
    public CommonResponse<DeliveryResult> updateDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody UpdateDeliveryRequest request
    ) {
        DeliveryResult result = deliveryApplicationService.updateDelivery(deliveryId, request.toCommand());
        return CommonResponse.success("배송 정보가 수정되었습니다.", result);
    }

    @Operation(summary = "배송 논리 삭제")
    @DeleteMapping("/{deliveryId}")
    public CommonResponse<DeliveryResult> deleteDelivery(
            @PathVariable UUID deliveryId
    ) {
        DeliveryResult result = deliveryApplicationService.deleteDelivery(deliveryId);
        return CommonResponse.success("배송 정보가 삭제되었습니다.", result);
    }
}
