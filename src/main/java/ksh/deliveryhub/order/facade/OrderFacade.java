package ksh.deliveryhub.order.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.service.OrderItemService;
import ksh.deliveryhub.order.service.OrderService;
import ksh.deliveryhub.point.service.UserPointService;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartMenuService cartMenuService;
    private final UserCouponService userCouponService;
    private final UserPointService userPointService;
    private final CartService cartService;

    @Transactional
    public Order placeOrder(long userId, long userCouponId, int pointToUse) {
        //장바구니 조회 및 검증
        Cart cart = cartService.getUserCart(userId);
        List<CartMenuDetail> cartMenuDetails = cartMenuService.checkCartMenuBeforeOrder(cart.getId());

        //사용할 쿠폰 검증 및 예약
        FoodCategory foodCategory = cartMenuDetails.getFirst().getFoodCategory();
        UserCouponDetail userCouponDetail = userCouponService.reserveCoupon(
            userId,
            userCouponId,
            foodCategory
        );

        //사용할 포인트 검증
        userPointService.checkBalanceBeforeOrder(userId, pointToUse);

        //주문 생성
        Long storeId = cartMenuDetails.getFirst().getStoreId();
        int totalPrice = cartMenuDetails.stream()
            .mapToInt(CartMenuDetail::getTotalPrice)
            .sum();
        Order order = orderService.createOrder(
            userId,
            storeId,
            totalPrice,
            userCouponDetail.getDiscountAmount(),
            pointToUse
        );

        orderItemService.createOrderItems(order.getId(), cartMenuDetails);

        return order;
    }
}
