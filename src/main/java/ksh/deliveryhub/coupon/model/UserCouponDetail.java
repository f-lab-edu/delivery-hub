package ksh.deliveryhub.coupon.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCouponDetail {

    private Coupon coupon;
    private UserCoupon userCoupon;

    public static UserCouponDetail of(Coupon coupon, UserCoupon userCoupon) {
        return UserCouponDetail.builder()
            .coupon(coupon)
            .userCoupon(userCoupon)
            .build();
    }
}
