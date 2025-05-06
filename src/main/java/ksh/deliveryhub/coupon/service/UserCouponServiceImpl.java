package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService{

    private final UserCouponRepository userCouponRepository;

    @Override
    public UserCoupon registerCoupon(long userId, Coupon coupon) {
        Integer duration = coupon.getDuration();
        LocalDateTime expireAt = LocalDateTime.now().plusDays(duration);

        UserCouponEntity userCouponEntity = UserCouponEntity.builder()
            .couponStatus(UserCouponStatus.ACTIVE)
            .userId(userId)
            .couponId(coupon.getId())
            .expireAt(expireAt)
            .build();
        userCouponRepository.save(userCouponEntity);

        return UserCoupon.from(userCouponEntity);
    }
}
