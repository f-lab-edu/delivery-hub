package ksh.deliveryhub.coupon.model;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserCoupon {

    private Long id;
    private Long userId;
    private Long couponId;
    private UserCouponStatus couponStatus;
    private LocalDate expireAt;

    public static UserCoupon from(UserCouponEntity userCouponEntity) {
        return UserCoupon.builder()
            .id(userCouponEntity.getId())
            .userId(userCouponEntity.getUserId())
            .couponId(userCouponEntity.getCouponId())
            .couponStatus(userCouponEntity.getCouponStatus())
            .expireAt(userCouponEntity.getExpireAt())
            .build();
    }

    public UserCouponEntity toEntity() {
        return UserCouponEntity.builder()
            .id(getId())
            .userId(getUserId())
            .couponId(getCouponId())
            .couponStatus(getCouponStatus())
            .expireAt(getExpireAt())
            .build();
    }
}
