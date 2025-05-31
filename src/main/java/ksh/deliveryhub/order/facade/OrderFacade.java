package ksh.deliveryhub.order.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.lock.DistributedLockService;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.order.dto.command.OrderCreateCommand;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.model.OrderDetailForDelivery;
import ksh.deliveryhub.order.model.OrderItemWithMenu;
import ksh.deliveryhub.order.model.OrderWithStoreInfo;
import ksh.deliveryhub.order.service.OrderItemService;
import ksh.deliveryhub.order.service.OrderService;
import ksh.deliveryhub.point.service.UserPointService;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartMenuService cartMenuService;
    private final UserCouponService userCouponService;
    private final UserPointService userPointService;
    private final CartService cartService;
    private final DistributedLockService lockService;

    @Transactional
    public Order placeOrder(long userId, Long userCouponId, int pointToUse) {
        //장바구니 조회 및 검증
        Cart cart = cartService.getUserCart(userId);
        List<CartMenuDetail> cartMenuDetails = cartMenuService.checkCartMenuBeforeOrder(cart.getId());

        //사용할 쿠폰 검증 및 예약
        FoodCategory foodCategory = cartMenuDetails.getFirst().getStore().getFoodCategory();
        UserCouponDetail userCouponDetail = userCouponService.reserveCoupon(
            userCouponId,
            userId,
            foodCategory
        );

        //사용할 포인트 검증
        userPointService.checkBalanceBeforeOrder(userId, pointToUse);

        //주문 생성
        OrderCreateCommand command = OrderCreateCommand.of(userCouponDetail, cartMenuDetails, pointToUse);
        Order order = orderService.createOrder(command);

        orderItemService.createOrderItems(order.getId(), cartMenuDetails);

        return order;
    }

    @Transactional
    public void acceptOrder(long id, long storeId) {
        orderService.acceptOrder(id, storeId);
    }

    @Transactional
    public void assignRiderToOrder(long orderId, long riderId) {
        String lockKey = "order:" + orderId;
        lockService.acquire(
            lockKey,
            1000L,
            5000L,
            TimeUnit.MILLISECONDS,
            () -> orderService.assignRiderToOrder(orderId, riderId)
        );
    }

    @Transactional(readOnly = true)
    public PageResult<OrderDetailForDelivery> findOrdersWaitingForDelivery(Location location, PageRequestDto pageRequestDto) {
        PageResult<OrderWithStoreInfo> pageResult = orderService.findOrdersWaitingForDelivery(location, pageRequestDto);
        List<Long> orderIds = pageResult.getContent()
            .stream()
            .map(OrderWithStoreInfo::getOrder)
            .map(Order::getId)
            .toList();

        Map<Long, List<OrderItemWithMenu>> orderItemMap = orderItemService.getOrderItemsIn(orderIds).stream()
            .collect(Collectors.groupingBy(
                orderItem -> orderItem.getOrderItem().getOrderId()
            ));

        return pageResult.map(ows -> {
            Long orderId = ows.getOrder().getId();
            List<OrderItemWithMenu> items =
                orderItemMap.getOrDefault(orderId, Collections.emptyList());
            return OrderDetailForDelivery.of(ows, items);
        });
    }
}
