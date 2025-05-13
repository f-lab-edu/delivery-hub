package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.Coupon;

public interface CouponService {

    Coupon createCoupon(Coupon coupon);

    Coupon issueCoupon(String code);

    Coupon getById(long id);
}
