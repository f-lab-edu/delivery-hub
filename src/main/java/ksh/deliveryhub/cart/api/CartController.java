package ksh.deliveryhub.cart.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.cart.dto.request.CartMenuCreateRequestDto;
import ksh.deliveryhub.cart.dto.response.CartMenuResponseDto;
import ksh.deliveryhub.cart.dto.response.CartResponseDto;
import ksh.deliveryhub.cart.facade.CartFacade;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @GetMapping("/carts/{cartId}/menus")
    public ResponseEntity<SuccessResponseDto> getUserCart(
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

    @PostMapping("/carts/{cartId}/menus")
    public ResponseEntity<SuccessResponseDto> addMenu(
        @PathVariable("cartId") long cartId,
        @RequestParam("userId") long userId,
        @Valid @RequestBody CartMenuCreateRequestDto request
    ) {
        cartFacade.addMenuToCart(userId, request.toModel());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
