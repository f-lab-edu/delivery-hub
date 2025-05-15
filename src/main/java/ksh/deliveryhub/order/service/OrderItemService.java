package ksh.deliveryhub.order.service;

import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.order.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> createOrderItems(long orderId, List<CartMenuDetail> cartMenuDetails);
}
