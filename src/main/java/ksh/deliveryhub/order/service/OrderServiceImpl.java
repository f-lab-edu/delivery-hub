package ksh.deliveryhub.order.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.order.dto.command.OrderCreateCommand;
import ksh.deliveryhub.order.dto.query.AcceptedOrderQuery;
import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.model.OrderWithStoreInfo;
import ksh.deliveryhub.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

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
        OrderEntity orderEntity = orderRepository.findByIdAndStoreIdAndOrderStatus(id, userId, OrderStatus.PENDING)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return Order.from(orderEntity);
    }

    @Transactional
    @Override
    public void completePayment(long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        orderEntity.updateStatus(OrderStatus.PAID);
    }

    @Override
    public void acceptOrder(long id, long storeId) {
        orderRepository.findByIdAndStoreIdAndOrderStatus(id, storeId, OrderStatus.PAID)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND))
            .updateStatus(OrderStatus.ACCEPTED);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResult<OrderWithStoreInfo> findOrdersWaitingForDelivery(
        AcceptedOrderQuery query,
        PageRequestDto pageRequestDto
    ) {
        Pageable pageable = PageRequest.of(
            pageRequestDto.getPage(),
            pageRequestDto.getSize()
        );

        return orderRepository.findByStatusAndCurrentLocation(
                OrderStatus.ACCEPTED,
                query.getLocation(),
                query.getLastCreatedAt(),
                pageable
            )
            .map(OrderWithStoreInfo::from);
    }

    @Override
    public void assignRiderToOrder(long id, long riderId) {
        OrderEntity orderEntity = orderRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (orderEntity.getRiderId() != null) {
            throw new CustomException(ErrorCode.ORDER_RIDER_ALREADY_ASSIGNED);
        }
        orderEntity.assignRider(riderId);
    }
}
