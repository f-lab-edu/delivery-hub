package ksh.deliveryhub.order.api;

import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.order.dto.request.OrderCreateRequestDto;
import ksh.deliveryhub.order.dto.response.OrderCreateResponseDto;
import ksh.deliveryhub.order.facade.OrderFacade;
import ksh.deliveryhub.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
