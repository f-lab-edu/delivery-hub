package ksh.deliveryhub.order.dto.response;

import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.model.OrderDetailForDelivery;
import ksh.deliveryhub.order.model.OrderItemWithMenu;
import ksh.deliveryhub.order.model.OrderWithStoreInfo;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AcceptedOrderResponseDto {

    private long id;
    private String storeName;
    private String address;
    private String phone;
    private List<OrderItemWithMenuResponseDto> menus;



    public static AcceptedOrderResponseDto from(OrderDetailForDelivery orderDetailForDelivery) {
        OrderWithStoreInfo orderWithStoreInfo = orderDetailForDelivery.getOrderWithStoreInfo();
        List<OrderItemWithMenu> orderItemsWithMenu = orderDetailForDelivery.getOrderItemsWithMenu();
        Order order = orderWithStoreInfo.getOrder();
        Store store = orderWithStoreInfo.getStore();

        List<OrderItemWithMenuResponseDto> menuResponseDtos = orderItemsWithMenu.stream()
            .map(OrderItemWithMenuResponseDto::from)
            .toList();

        return AcceptedOrderResponseDto.builder()
            .id(order.getId())
            .storeName(store.getName())
            .address(store.getAddress().toString())
            .phone(store.getPhone())
            .menus(menuResponseDtos)
            .build();
    }
}
