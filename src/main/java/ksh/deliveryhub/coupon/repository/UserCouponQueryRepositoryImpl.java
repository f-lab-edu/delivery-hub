package ksh.deliveryhub.coupon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.repository.projection.QUserCouponDetailProjection;
import ksh.deliveryhub.coupon.repository.projection.UserCouponDetailProjection;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static ksh.deliveryhub.coupon.entity.QCouponEntity.couponEntity;
import static ksh.deliveryhub.coupon.entity.QUserCouponEntity.userCouponEntity;

@RequiredArgsConstructor
public class UserCouponQueryRepositoryImpl implements UserCouponQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserCouponDetailProjection> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory) {
        BooleanExpression foodCategoryEquals = foodCategory == null ? null : couponEntity.foodCategory.eq(foodCategory);

        return queryFactory
            .select(new QUserCouponDetailProjection(userCouponEntity, couponEntity))
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

    @Override
    public Optional<UserCouponDetailProjection> findCouponToApply(long id, long userId, FoodCategory foodCategory) {
        UserCouponDetailProjection userCouponDetailProjection = queryFactory
            .select(new QUserCouponDetailProjection(userCouponEntity, couponEntity))
            .from(userCouponEntity)
            .join(couponEntity).on(userCouponEntity.couponId.eq(couponEntity.id))
            .where(
                userCouponEntity.id.eq(id),
                userCouponEntity.userId.eq(userId),
                userCouponEntity.couponStatus.eq(UserCouponStatus.ACTIVE),
                couponEntity.foodCategory.eq(foodCategory)
            )
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetchOne();

        return Optional.ofNullable(userCouponDetailProjection);
    }

    @Override
    public UserCouponEntity getReservedCouponForPayment(long id, long userId) {
        UserCouponEntity userCoupon = queryFactory
            .selectFrom(userCouponEntity)
            .where(
                userCouponEntity.id.eq(id),
                userCouponEntity.userId.eq(userId),
                userCouponEntity.couponStatus.eq(UserCouponStatus.RESERVED)
            )
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetchOne();

        if (userCoupon == null) {
            throw new CustomException(ErrorCode.USER_COUPON_NOT_FOUND);
        }
        return userCoupon;
    }
}
