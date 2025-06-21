package ksh.deliveryhub.order.model;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.order.repository.projection.OrderItemWithMenuProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemWithMenu {

    private OrderItem orderItem;
    private Menu menu;

    public static OrderItemWithMenu from(OrderItemWithMenuProjection projection) {
        return OrderItemWithMenu.builder()
            .orderItem(OrderItem.from(projection.getOrderItemEntity()))
            .menu(Menu.from(projection.getMenuEntity()))
            .build();
    }
}
