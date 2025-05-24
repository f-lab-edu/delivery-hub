package ksh.deliveryhub.coupon.facade;

import ksh.deliveryhub.common.lock.DistributedLockService;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.service.CouponService;
import ksh.deliveryhub.coupon.service.UserCouponService;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final DistributedLockService lockService;

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @Transactional
    public void registerUserCoupon(long userId, String code) {
        lockService.acquire(
            code,
            1000L,
            50000L,
            TimeUnit.MILLISECONDS,
            () -> {
                Coupon coupon = couponService.issueCoupon(code);
                userCouponService.registerCoupon(userId, coupon);
            }
        );
    }

    @Transactional(readOnly = true)
    public List<UserCouponDetail> findAvailableCouponDetails(long userId, FoodCategory foodCategory) {
        return userCouponService.findAvailableCouponsWithDetail(userId, foodCategory);
    }
}
