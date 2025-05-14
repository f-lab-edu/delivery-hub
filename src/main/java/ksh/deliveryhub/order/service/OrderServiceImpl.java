package ksh.deliveryhub.order.service;

import ksh.deliveryhub.order.dto.command.OrderCreateCommand;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(OrderCreateCommand command) {
        final int totalPrice = command.getTotalPrice();
        final int discountAmount = command.getDiscountAmount();
        final int usedPoint = command.getUsedPoint();
        final int finalPrice = totalPrice - discountAmount - usedPoint;

        OrderEntity orderEntity = OrderEntity.builder()
            .totalPrice(totalPrice)
            .discountAmount(discountAmount)
            .usedPoint(usedPoint)
            .finalPrice(finalPrice)
            .orderStatus(OrderStatus.PENDING)
            .userId(command.getUserId())
            .storeId(command.getStoreId())
            .build();

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        return Order.from(savedOrderEntity);
    }

    @Override
    public Order getPendingOrder(long id, long userId) {
        OrderEntity orderEntity = orderRepository.findByIdAndUserIdAndOrderStatus(id, userId, OrderStatus.PENDING)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return Order.from(orderEntity);
    }
}
