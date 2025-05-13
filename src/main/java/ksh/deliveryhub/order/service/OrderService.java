package ksh.deliveryhub.order.service;

import ksh.deliveryhub.order.model.Order;

public interface OrderService {

    Order createOrder(long userId, long storeId, int totalPrice, int discountAmount, int usedPoint);
}
