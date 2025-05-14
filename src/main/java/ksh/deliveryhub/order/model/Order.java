package ksh.deliveryhub.order.model;

import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Order {

    private Long id;
    private Integer totalPrice;
    private Integer discountAmount;
    private Integer usedPoint;
    private Integer finalPrice;
    private OrderStatus orderStatus;
    private Long userId;
    private Long storeId;
    private Long riderId;

    public static Order from(OrderEntity orderEntity) {
        return Order.builder()
            .id(orderEntity.getId())
            .totalPrice(orderEntity.getTotalPrice())
            .discountAmount(orderEntity.getDiscountAmount())
            .usedPoint(orderEntity.getUsedPoint())
            .finalPrice(orderEntity.getFinalPrice())
            .orderStatus(orderEntity.getOrderStatus())
            .userId(orderEntity.getUserId())
            .storeId(orderEntity.getStoreId())
            .riderId(orderEntity.getRiderId())
            .build();
    }

    public OrderEntity toEntity() {
        return OrderEntity.builder()
            .id(getId())
            .totalPrice(getTotalPrice())
            .discountAmount(getDiscountAmount())
            .usedPoint(getUsedPoint())
            .finalPrice(getFinalPrice())
            .orderStatus(getOrderStatus())
            .userId(getUserId())
            .storeId(getStoreId())
            .riderId(getRiderId())
            .build();
    }
}
