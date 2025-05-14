package ksh.deliveryhub.order.service;

import ksh.deliveryhub.order.dto.command.OrderCreateCommand;
import ksh.deliveryhub.order.model.Order;

public interface OrderService {

    Order createOrder(OrderCreateCommand command);

    Order getPendingOrder(long id, long userId);

    void completePayment(long id);
}
