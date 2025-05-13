package ksh.deliveryhub.order.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.service.CouponService;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.service.OrderItemService;
import ksh.deliveryhub.order.service.OrderService;
import ksh.deliveryhub.point.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    private final CouponService couponService;

    public Order placeOrder(long userId, long userCouponId, int pointToUse) {
        Cart cart = cartService.getUserCart(userId);
        List<CartMenuDetail> cartMenuDetails = cartMenuService.checkCartMenuBeforeOrder(cart.getId());

        UserCoupon userCoupon = userCouponService.reserveCoupon(userId, userCouponId);
        Coupon coupon = couponService.getById(userCoupon.getCouponId());
        userPointService.checkBalanceBeforeOrder(userId, pointToUse);

        Long storeId = cartMenuDetails.getFirst().getStoreId();
        int totalPrice = cartMenuDetails.stream()
            .mapToInt(CartMenuDetail::getTotalPrice)
            .sum();

        Order order = orderService.createOrder(userId, storeId, totalPrice, coupon.getDiscountAmount(), pointToUse);
        orderItemService.createOrderItems(order.getId(), cartMenuDetails);

        return order;
    }
}
