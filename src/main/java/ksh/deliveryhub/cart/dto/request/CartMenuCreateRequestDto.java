package ksh.deliveryhub.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksh.deliveryhub.cart.model.CartMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuCreateRequestDto {

    @NotNull(message = "메뉴 id는 필수입니다.")
    private Long menuId;

    @NotNull(message = "메뉴 옵션 id는 필수입니다.")
    private Long optionId;

    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 양수입니다.")
    private Integer quantity;

    public CartMenu toModel() {
        return CartMenu.builder()
            .quantity(getQuantity())
            .menuId(getMenuId())
            .optionId(getOptionId())
            .build();
    }
}
