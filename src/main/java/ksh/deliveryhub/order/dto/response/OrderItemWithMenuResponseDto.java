package ksh.deliveryhub.order.dto.response;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.order.model.OrderItem;
import ksh.deliveryhub.order.model.OrderItemWithMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemWithMenuResponseDto {

    private String name;
    private int quantity;

    public static OrderItemWithMenuResponseDto from(OrderItemWithMenu orderItemWithMenu) {
        OrderItem orderItem = orderItemWithMenu.getOrderItem();
        Menu menu = orderItemWithMenu.getMenu();

        return OrderItemWithMenuResponseDto.builder()
            .name(menu.getName())
            .quantity(orderItem.getQuantity())
            .build();
    }
}
