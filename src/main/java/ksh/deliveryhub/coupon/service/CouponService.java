package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.Coupon;

import java.util.List;

public interface CouponService {

    Coupon createCoupon(Coupon coupon);

    Coupon issueCoupon(String code);

    List<Coupon> findCouponsByIdsIn(List<Long> ids);
}
