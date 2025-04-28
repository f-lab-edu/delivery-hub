package ksh.deliveryhub.order.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Long orderId;

    private Long menuId;

    @Builder
    private OrderItemEntity(
        Integer quantity,
        Long orderId,
        Long menuId
    ) {
        this.quantity = quantity;
        this.orderId = orderId;
        this.menuId = menuId;
    }
}
