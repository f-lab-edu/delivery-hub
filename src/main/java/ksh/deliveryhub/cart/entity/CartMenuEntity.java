package ksh.deliveryhub.cart.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_menu")
public class CartMenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Long cartId;

    private Long menuId;

    private Long orderId;

    @Builder
    private CartMenuEntity(
        Long id,
        Integer quantity,
        Long cartId,
        Long menuId,
        Long orderId
    ) {
        this.id = id;
        this.quantity = quantity;
        this.cartId = cartId;
        this.menuId = menuId;
        this.orderId = orderId;
    }
}
