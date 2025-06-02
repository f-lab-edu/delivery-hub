package ksh.deliveryhub.order.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.order.dto.command.OrderCreateCommand;
import ksh.deliveryhub.order.dto.query.AcceptedOrderQuery;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.model.OrderWithStoreInfo;

public interface OrderService {

    Order createOrder(OrderCreateCommand command);

    Order getPendingOrder(long id, long userId);

    void completePayment(long id);

    void acceptOrder(long id, long storeId);

    PageResult<OrderWithStoreInfo> findOrdersWaitingForDelivery(AcceptedOrderQuery query, PageRequestDto pageRequestDto);

    void assignRiderToOrder(long id, long riderId);
}
