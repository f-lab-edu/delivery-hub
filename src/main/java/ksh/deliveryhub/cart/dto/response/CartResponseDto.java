package ksh.deliveryhub.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponseDto {

    private List<CartMenuResponseDto> menus;

    public static CartResponseDto of(List<CartMenuResponseDto> cartMenuResponseDtos) {
        return CartResponseDto.builder()
            .menus(cartMenuResponseDtos)
            .build();
    }
}
