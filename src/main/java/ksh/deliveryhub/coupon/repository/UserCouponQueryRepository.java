package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.repository.projection.UserCouponDetailProjection;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;
import java.util.Optional;

public interface UserCouponQueryRepository {

    List<UserCouponDetailProjection> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory);

    Optional<UserCouponDetailProjection> findCouponToApply(long id, long userId, FoodCategory foodCategory);
}
