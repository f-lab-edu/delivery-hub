package ksh.deliveryhub.coupon.facade;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.service.CouponService;
import ksh.deliveryhub.coupon.service.CouponTransactionService;
import ksh.deliveryhub.coupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final CouponTransactionService couponTransactionService;

    public UserCouponDetail registerUserCoupon(long userId, String code) {
        Coupon coupon = couponService.issueCoupon(code);
        UserCoupon userCoupon = userCouponService.registerCoupon(userId, coupon);
        couponTransactionService.saveIssueTransaction(userCoupon);

        return UserCouponDetail.of(coupon, userCoupon);
    }
}
