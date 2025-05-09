package ksh.deliveryhub.cart.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.cart.dto.request.CartMenuCreateRequestDto;
import ksh.deliveryhub.cart.dto.request.CartMenuUpdateRequestDto;
import ksh.deliveryhub.cart.dto.response.CartMenuResponseDto;
import ksh.deliveryhub.cart.dto.response.CartResponseDto;
import ksh.deliveryhub.cart.facade.CartFacade;
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

    @GetMapping("/users/{userId}/carts/menus")
    public ResponseEntity<SuccessResponseDto> getUserCartDetails(
        @PathVariable("userId") long userId
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

    @PostMapping("/users/{userId}/carts/menus")
    public ResponseEntity<SuccessResponseDto> addMenu(
        @RequestParam("userId") long userId,
        @Valid @RequestBody CartMenuCreateRequestDto request
    ) {
        cartFacade.addMenuToCart(userId, request.toModel());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping("/users/{userId}/carts/menus/{cartMenuId}")
    public ResponseEntity<SuccessResponseDto> changeMenuQuantity(
        @PathVariable("cartMenuId") long cartMenuId,
        @RequestParam("userId") long userId,
        @Valid @RequestBody CartMenuUpdateRequestDto request
    ) {
        cartFacade.changeQuantity(userId, request.toModel(cartMenuId));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping("/users/{userId}/carts/menus")
    public ResponseEntity<SuccessResponseDto> clearCart(
        @RequestParam("userId") long userId,
        @Valid @RequestBody CartMenuUpdateRequestDto request
    ) {
        cartFacade.clearCart(userId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/users/{userId}/carts/menus/{cartMenuId}")
    public ResponseEntity<SuccessResponseDto> deleteMenu(
        @PathVariable("cartMenuId") long cartMenuId,
        @RequestParam("userId") long userId
    ) {
        cartFacade.deleteMenuInCart(userId, cartMenuId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
