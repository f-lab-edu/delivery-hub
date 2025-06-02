package ksh.deliveryhub.order.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.order.dto.request.OrderCreateRequestDto;
import ksh.deliveryhub.order.dto.request.OrderQueryRequestDto;
import ksh.deliveryhub.order.dto.response.AcceptedOrderResponseDto;
import ksh.deliveryhub.order.dto.response.OrderCreateResponseDto;
import ksh.deliveryhub.order.facade.OrderFacade;
import ksh.deliveryhub.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<SuccessResponseDto> placeOrder(
        @PathVariable("userId") long userId,
        @RequestBody OrderCreateRequestDto request
    ) {
        Order order = orderFacade.placeOrder(
            userId,
            request.getUserCouponId(),
            request.getPointToUse() != null ? request.getPointToUse() : 0
        );

        OrderCreateResponseDto responseDto = OrderCreateResponseDto.from(order);
        SuccessResponseDto<OrderCreateResponseDto> response = SuccessResponseDto.of(responseDto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/store/{storeId}/orders/{orderId}")
    public ResponseEntity<SuccessResponseDto> accpetOrder(
        @PathVariable("storeId") long storeId,
        @PathVariable("orderId") long orderId
    ) {
        orderFacade.acceptOrder(orderId, storeId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @PostMapping("/riders/{riderId}/orders/{orderId}")
    public ResponseEntity<SuccessResponseDto> assignOrderToRider(
        @PathVariable("riderId") long riderId,
        @PathVariable("orderId") long orderId
    ) {
        orderFacade.assignRiderToOrder(orderId, riderId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @GetMapping("/orders/paid")
    public ResponseEntity<SuccessResponseDto> findOrdersWaitingForDelivery(
        @Valid OrderQueryRequestDto orderQueryRequestDto,
        @Valid PageRequestDto pageRequestDto
    ) {
        PageResult<AcceptedOrderResponseDto> pageResult = orderFacade.findOrdersWaitingForDelivery(
                orderQueryRequestDto.getCurrentLocation(),
                pageRequestDto
            )
            .map(AcceptedOrderResponseDto::from);

        SuccessResponseDto<PageResult<AcceptedOrderResponseDto>> response = SuccessResponseDto.of(pageResult);

        return  ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
