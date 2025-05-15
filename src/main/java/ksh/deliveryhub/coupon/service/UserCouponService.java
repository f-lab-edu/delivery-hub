package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;

public interface UserCouponService {

    UserCoupon registerCoupon(long userId, Coupon coupon);

    List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory);

    UserCouponDetail reserveCoupon(Long id, long userId, FoodCategory foodCategory);

    UserCoupon useCoupon(long id, long userId);
}
