package ksh.deliveryhub.order.model;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
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
        CartMenu cartMenu = cartMenuDetail.getCartMenu();
        Menu menu = cartMenuDetail.getMenu();
        MenuOption menuOption = cartMenuDetail.getMenuOption();

        return OrderItem.builder()
            .quantity(cartMenu.getQuantity())
            .menuPrice(menu.getPrice())
            .optionPrice(menuOption != null ? menuOption.getPrice() : null)
            .orderId(orderId)
            .menuId(menu.getId())
            .optionId(menuOption != null ? menuOption.getId() : null)
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
