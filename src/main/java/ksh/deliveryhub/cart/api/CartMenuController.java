package ksh.deliveryhub.cart.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.cart.dto.request.CartMenuUpdateRequestDto;
import ksh.deliveryhub.cart.facade.CartFacade;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartMenuController {

    private final CartFacade cartFacade;

    @PostMapping("/users/{userId}/carts/menus/{cartMenuId}")
    public ResponseEntity<SuccessResponseDto> changeMenuQuantity(
        @PathVariable("userId") long userId,
        @PathVariable("cartMenuId") long cartMenuId,
        @Valid @RequestBody CartMenuUpdateRequestDto request
    ) {
        cartFacade.changeQuantity(userId, request.toModel(cartMenuId));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/users/{userId}/carts/menus/{cartMenuId}")
    public ResponseEntity<SuccessResponseDto> deleteMenu(
        @PathVariable("userId") long userId,
        @PathVariable("cartMenuId") long cartMenuId
    ) {
        cartFacade.deleteMenuInCart(userId, cartMenuId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
