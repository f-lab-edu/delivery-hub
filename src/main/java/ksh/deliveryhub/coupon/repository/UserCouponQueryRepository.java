package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;

public interface UserCouponQueryRepository {

    List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory);
}
