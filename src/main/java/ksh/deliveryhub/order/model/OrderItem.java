package ksh.deliveryhub.order.model;

import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.order.entity.OrderItemEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItem {

    private Long id;
    private Integer quantity;
    private Integer menuPrice;
    private Integer optionPrice;
    private Long orderId;
    private Long menuId;
    private Long optionId;

    public static OrderItem from(long orderId, CartMenuDetail cartMenuDetail) {
        return OrderItem.builder()
            .quantity(cartMenuDetail.getQuantity())
            .menuPrice(cartMenuDetail.getMenuPrice())
            .optionPrice(cartMenuDetail.getOptionPrice())
            .orderId(orderId)
            .menuId(cartMenuDetail.getMenuId())
            .optionId(cartMenuDetail.getOptionId())
            .build();
    }

    public static OrderItem from(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
            .id(orderItemEntity.getId())
            .quantity(orderItemEntity.getQuantity())
            .menuPrice(orderItemEntity.getMenuPrice())
            .optionPrice(orderItemEntity.getOptionPrice())
            .orderId(orderItemEntity.getOrderId())
            .menuId(orderItemEntity.getMenuId())
            .optionId(orderItemEntity.getOptionId())
            .build();
    }

    public OrderItemEntity toEntity() {
        return OrderItemEntity.builder()
            .id(getId())
            .quantity(getQuantity())
            .menuPrice(getMenuPrice())
            .optionPrice(getOptionPrice())
            .orderId(getOrderId())
            .menuId(getMenuId())
            .optionId(getOptionId())
            .build();
    }
}
