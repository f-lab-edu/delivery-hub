package ksh.deliveryhub.coupon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static ksh.deliveryhub.coupon.entity.QCouponEntity.couponEntity;
import static ksh.deliveryhub.coupon.entity.QUserCouponEntity.userCouponEntity;

@RequiredArgsConstructor
public class UserCouponQueryRepositoryImpl implements UserCouponQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserCouponEntity> findApplicableCoupons(long userId, FoodCategory foodCategory) {
        BooleanExpression foodCategoryEquals = foodCategory == null ? null : couponEntity.foodCategory.eq(foodCategory);

        return queryFactory
            .select(userCouponEntity)
            .from(userCouponEntity)
            .join(couponEntity)
            .on(userCouponEntity.couponId.eq(couponEntity.id))
            .where(
                userCouponEntity.userId.eq(userId),
                userCouponEntity.couponStatus.eq(UserCouponStatus.ACTIVE),
                foodCategoryEquals
            )
            .fetch();
    }
}
