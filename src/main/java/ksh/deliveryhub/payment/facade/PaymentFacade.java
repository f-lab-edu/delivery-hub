package ksh.deliveryhub.payment.facade;

import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.service.CouponTransactionService;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.order.model.Order;
import ksh.deliveryhub.order.service.OrderService;
import ksh.deliveryhub.payment.entity.PaymentMethod;
import ksh.deliveryhub.payment.model.Payment;
import ksh.deliveryhub.payment.service.PaymentService;
import ksh.deliveryhub.point.service.UserPointService;
import ksh.deliveryhub.point.service.UserPointTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final UserCouponService userCouponService;
    private final CouponTransactionService couponTransactionService;
    private final UserPointService userPointService;
    private final UserPointTransactionService userPointTransactionService;
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;

    @Transactional
    public Payment processPayment(
        long userId,
        long orderId,
        Long userCouponId,
        Integer pointToUse,
        PaymentMethod paymentMethod
    ) {
        Order pendingOrder = orderService.getPendingOrder(orderId, userId);

        UserCoupon userCoupon = userCouponService.useCoupon(userCouponId, userId);
        couponTransactionService.saveUseTransaction(userCoupon, orderId);

        if (pointToUse == null) {
            int earnedPoint = userPointService.earnPoint(userId, pendingOrder.getFinalPrice());
            userPointTransactionService.saveEarnTransaction(earnedPoint, orderId, userId);
        } else {
            userPointService.usePoint(userId, pointToUse);
            userPointTransactionService.saveUseTransaction(pointToUse, orderId, userId);
        }

        orderService.completePayment(orderId);
        cartService.closeCartAfterPayment(userId);

        return paymentService.savePayment(paymentMethod, pendingOrder.getFinalPrice(), orderId);
    }
}
