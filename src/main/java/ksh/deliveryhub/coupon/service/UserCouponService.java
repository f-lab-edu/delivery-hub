package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;

public interface UserCouponService {

    UserCoupon registerCoupon(long userId, Coupon coupon);
}
