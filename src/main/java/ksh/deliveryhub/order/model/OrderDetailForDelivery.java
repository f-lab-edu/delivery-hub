package ksh.deliveryhub.order.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderDetailForDelivery {

    private OrderWithStoreInfo orderWithStoreInfo;
    private List<OrderItemWithMenu> orderItemsWithMenu;

    public static OrderDetailForDelivery of(
        OrderWithStoreInfo orderWithStoreInfo,
        List<OrderItemWithMenu> orderItemsWithMenu
    ) {
        return OrderDetailForDelivery.builder()
            .orderWithStoreInfo(orderWithStoreInfo)
            .orderItemsWithMenu(orderItemsWithMenu)
            .build();
    }
}
