package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;
import java.util.Optional;

public interface UserCouponQueryRepository {

    List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory);

    Optional<UserCouponEntity> findAvailableCouponByIdAndUserId(long id, long userId);
}
