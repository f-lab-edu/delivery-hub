package ksh.deliveryhub.order.service;

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
    public Order createOrder(long userId, long storeId, int totalPrice, int discountAmount, int usedPoint) {
        final int finalPrice = totalPrice - discountAmount - usedPoint;

        OrderEntity orderEntity = OrderEntity.builder()
            .totalPrice(totalPrice)
            .discountAmount(discountAmount)
            .usedPoint(usedPoint)
            .finalPrice(finalPrice)
            .orderStatus(OrderStatus.PENDING)
            .userId(userId)
            .storeId(storeId)
            .build();

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        return Order.from(savedOrderEntity);
    }
}
