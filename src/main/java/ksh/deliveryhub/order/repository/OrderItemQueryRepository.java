package ksh.deliveryhub.order.repository;

import ksh.deliveryhub.order.repository.projection.OrderItemWithMenuProjection;

import java.util.List;

public interface OrderItemQueryRepository {

    List<OrderItemWithMenuProjection> getOrderItemWithMenuIn(List<Long> orderIds);
}
