package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;

public interface UserCouponQueryRepository {

    List<UserCouponEntity> findApplicableCoupons(long userId, FoodCategory foodCategory);
}
