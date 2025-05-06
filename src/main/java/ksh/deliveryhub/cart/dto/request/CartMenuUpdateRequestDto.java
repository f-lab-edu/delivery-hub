package ksh.deliveryhub.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksh.deliveryhub.cart.model.CartMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuUpdateRequestDto {

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 양수입니다.")
    private Integer quantity;

    public CartMenu toModel(long id) {
        return CartMenu.builder()
            .id(id)
            .quantity(getQuantity())
            .build();
    }
}
