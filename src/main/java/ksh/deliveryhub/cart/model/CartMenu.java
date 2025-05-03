package ksh.deliveryhub.cart.model;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenu {

    private Long id;
    private Integer quantity;
    private Long cartId;
    private Long menuId;
    private Long optionId;

    public static CartMenu from(CartMenuEntity cartMenuEntity) {
        return CartMenu.builder()
            .id(cartMenuEntity.getId())
            .quantity(cartMenuEntity.getQuantity())
            .cartId(cartMenuEntity.getCartId())
            .menuId(cartMenuEntity.getMenuId())
            .optionId(cartMenuEntity.getOptionId())
            .build();
    }

    public CartMenuEntity toEntity() {
        return CartMenuEntity.builder()
            .id(getId())
            .quantity(getQuantity())
            .cartId(getCartId())
            .menuId(getMenuId())
            .optionId(getOptionId())
            .build();
    }
}
