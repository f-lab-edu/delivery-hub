package ksh.deliveryhub.cart.model;

import ksh.deliveryhub.cart.entity.CartEntity;
import ksh.deliveryhub.cart.entity.CartStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Cart {

    private Long id;
    private CartStatus status;
    private LocalDateTime expireAt;
    private Long userId;

    public static Cart from(CartEntity cartEntity) {
        return Cart.builder()
            .id(cartEntity.getId())
            .status(cartEntity.getStatus())
            .expireAt(cartEntity.getExpireAt())
            .userId(cartEntity.getUserId())
            .build();
    }

    public CartEntity toEntity() {
        return CartEntity.builder()
            .id(getId())
            .status(getStatus())
            .expireAt(getExpireAt())
            .userId(getUserId())
            .build();
    }
}
