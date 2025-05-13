package ksh.deliveryhub.order.service;

import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.order.entity.OrderItemEntity;
import ksh.deliveryhub.order.model.OrderItem;
import ksh.deliveryhub.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> createOrderItems(long orderId, List<CartMenuDetail> cartMenuDetails) {
        List<OrderItemEntity> orderItemEntities = cartMenuDetails.stream()
            .map(cartMenuDetail -> OrderItem.from(orderId, cartMenuDetail))
            .map(OrderItem::toEntity)
            .toList();

        return orderItemRepository.saveAll(orderItemEntities).stream()
            .map(OrderItem::from)
            .toList();
    }
}
