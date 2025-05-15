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
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalPrice;

    private Integer discountAmount;

    private Integer usedPoint;

    private Integer finalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Long userId;

    private Long storeId;

    private Long riderId;

    @Builder
    private OrderEntity(
        Long id,
        Integer totalPrice,
        Integer discountAmount,
        Integer usedPoint,
        Integer finalPrice,
        OrderStatus orderStatus,
        Long userId,
        Long storeId,
        Long riderId
    ) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.discountAmount = discountAmount;
        this.usedPoint = usedPoint;
        this.finalPrice = finalPrice;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.storeId = storeId;
        this.riderId = riderId;
    }
}
