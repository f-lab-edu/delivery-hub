package ksh.deliveryhub.coupon.service;

import com.querydsl.core.Tuple;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final Clock clock;

    @Override
    public UserCoupon registerCoupon(long userId, Coupon coupon) {
        userCouponRepository.findByUserIdAndCouponId(userId, coupon.getId())
            .ifPresent(userCouponEntity ->
                {throw new CustomException(ErrorCode.USER_COUPON_ALREADY_REGISTERED);}
            );


        Integer duration = coupon.getDuration();
        LocalDate expireAt = LocalDate.now(clock).plusDays(duration);

        UserCouponEntity userCouponEntity = UserCouponEntity.builder()
            .couponStatus(UserCouponStatus.ACTIVE)
            .userId(userId)
            .couponId(coupon.getId())
            .expireAt(expireAt)
            .build();
        userCouponRepository.save(userCouponEntity);

        return UserCoupon.from(userCouponEntity);
    }

    @Override
    public List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory) {
        return userCouponRepository.findAvailableCouponsWithDetail(userId, foodCategory);
    }

    @Override
    public UserCouponDetail reserveCoupon(long id, long userId, FoodCategory foodCategory) {
        Tuple tuple = userCouponRepository.findCouponToApply(id, userId, foodCategory)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_COUPON_NOT_USABLE));

        UserCouponEntity userCouponEntity = tuple.get(0, UserCouponEntity.class);
        userCouponEntity.reserve();

        int discountAmount = tuple.get(1, Integer.class);

        return UserCouponDetail.of(userCouponEntity, discountAmount);
    }
}
