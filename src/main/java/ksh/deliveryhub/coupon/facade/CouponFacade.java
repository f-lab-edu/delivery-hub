package ksh.deliveryhub.coupon.facade;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.service.CouponService;
import ksh.deliveryhub.coupon.service.CouponTransactionService;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final CouponTransactionService couponTransactionService;

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @Transactional
    public void registerUserCoupon(long userId, String code) {
        Coupon coupon = couponService.issueCoupon(code);
        UserCoupon userCoupon = userCouponService.registerCoupon(userId, coupon);
        couponTransactionService.saveIssueTransaction(userCoupon);
    }

    @Transactional(readOnly = true)
    public List<UserCouponDetail> findAvailableCouponDetails(long userId, FoodCategory foodCategory) {
        return userCouponService.findAvailableCouponsWithDetail(userId, foodCategory);
    }
}
