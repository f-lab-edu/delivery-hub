package ksh.deliveryhub.coupon.model;

import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCouponDetail {

    private final UserCoupon userCoupon;
    private final Coupon coupon;

    public static UserCouponDetail fromEntities(
        UserCouponEntity userCouponEntity,
        CouponEntity couponEntity
    ) {
        UserCoupon userCoupon = UserCoupon.from(userCouponEntity);
        Coupon coupon = Coupon.from(couponEntity);

        return UserCouponDetail.builder()
            .userCoupon(userCoupon)
            .coupon(coupon)
            .build();
    }

    public static UserCouponDetail empty() {
        return UserCouponDetail.builder()
            .userCoupon(UserCoupon.empty())
            .coupon(Coupon.empty())
            .build();
    }
}
