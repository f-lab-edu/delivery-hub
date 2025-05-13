package ksh.deliveryhub.coupon.repository;

import com.querydsl.core.Tuple;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.store.entity.FoodCategory;

import java.util.List;
import java.util.Optional;

public interface UserCouponQueryRepository {

    List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory);

    Optional<Tuple> findCouponToApply(long id, long userId, FoodCategory foodCategory);
}
