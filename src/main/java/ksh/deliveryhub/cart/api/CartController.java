package ksh.deliveryhub.cart.api;

import ksh.deliveryhub.cart.dto.response.CartMenuResponseDto;
import ksh.deliveryhub.cart.dto.response.CartResponseDto;
import ksh.deliveryhub.cart.facade.CartFacade;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @GetMapping("/carts/{cartId}/menus")
    public ResponseEntity getUserCart(
        @PathVariable("cartId") long cartId,
        @RequestParam("userId") long userId
    ) {
        List<CartMenuResponseDto> cartMenuResponseDtos = cartFacade.getUserCartMenuDetails(userId).stream()
            .map(CartMenuResponseDto::from)
            .toList();

        CartResponseDto cartResponseDto = CartResponseDto.of(cartMenuResponseDtos);
        SuccessResponseDto<CartResponseDto> response = SuccessResponseDto.of(cartResponseDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
