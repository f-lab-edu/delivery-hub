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
    private Long orderId;

    private static CartMenu from(CartMenuEntity cartMenuEntity) {
        return CartMenu.builder()
            .id(cartMenuEntity.getId())
            .quantity(cartMenuEntity.getQuantity())
            .cartId(cartMenuEntity.getCartId())
            .menuId(cartMenuEntity.getMenuId())
            .orderId(cartMenuEntity.getOrderId())
            .build();
    }

    private CartMenu toEntity() {
        return CartMenu.builder()
            .id(getId())
            .quantity(getQuantity())
            .cartId(getCartId())
            .menuId(getMenuId())
            .orderId(getOrderId())
            .build();
    }
}
