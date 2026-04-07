package org.iimsa.deliveryserver.deliverymanager.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iimsa.common.response.CommonResponse;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.FindDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.query.ListDeliveryManagerQuery;
import org.iimsa.deliveryserver.deliverymanager.application.dto.result.DeliveryManagerResult;
import org.iimsa.deliveryserver.deliverymanager.application.service.DeliveryManagerApplicationService;
import org.iimsa.deliveryserver.deliverymanager.presentation.dto.request.CreateDeliveryManagerRequest;
import org.iimsa.deliveryserver.deliverymanager.presentation.dto.request.UpdateDeliveryManagerRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(summary = "배송 담당자 단건 조회")
    @GetMapping("/{deliveryManagerId}")
    public CommonResponse<DeliveryManagerResult> findDeliveryManager(
            @PathVariable UUID deliveryManagerId
    ) {
        DeliveryManagerResult result = deliveryManagerApplicationService.findDeliveryManager(
                new FindDeliveryManagerQuery(deliveryManagerId)
        );
        return CommonResponse.success(result);
    }

    @Operation(summary = "배송 담당자 목록 조회")
    @GetMapping
    public CommonResponse<Page<DeliveryManagerResult>> listDeliveryManagers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DeliveryManagerResult> result = deliveryManagerApplicationService.listDeliveryManagers(
                new ListDeliveryManagerQuery(page, size)
        );
        return CommonResponse.success(result);
    }

    @Operation(summary = "배송 담당자 수정")
    @PatchMapping("/{deliveryManagerId}")
    public CommonResponse<DeliveryManagerResult> updateDeliveryManager(
            @PathVariable UUID deliveryManagerId,
            @RequestBody UpdateDeliveryManagerRequest request
    ) {
        DeliveryManagerResult result = deliveryManagerApplicationService.updateDeliveryManager(
                deliveryManagerId, request.toCommand()
        );
        return CommonResponse.success("배송 담당자 정보가 수정되었습니다.", result);
    }

    @Operation(summary = "배송 담당자 논리 삭제")
    @DeleteMapping("/{deliveryManagerId}")
    public CommonResponse<DeliveryManagerResult> deleteDeliveryManager(
            @PathVariable UUID deliveryManagerId
    ) {
        DeliveryManagerResult result = deliveryManagerApplicationService.deleteDeliveryManager(deliveryManagerId);
        return CommonResponse.success("배송 담당자가 삭제되었습니다.", result);
    }
}
