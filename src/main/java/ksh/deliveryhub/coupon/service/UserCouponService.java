package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;

public interface UserCouponService {

    UserCoupon registerCoupon(long userId, Coupon coupon);

    List<UserCoupon> findAvailableCouponsOfUser(long userId, FoodCategory foodCategory);
}
