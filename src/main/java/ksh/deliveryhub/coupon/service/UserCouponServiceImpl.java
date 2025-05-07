package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

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

    @Override
    public List<UserCoupon> findAvailableCouponsOfUser(long userId, FoodCategory foodCategory) {
        return userCouponRepository.findApplicableCoupons(
                userId,
                foodCategory
            )
            .stream()
            .map(UserCoupon::from)
            .toList();
    }
}
