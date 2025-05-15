package ksh.deliveryhub.coupon.dto.response;

import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserCouponDetailResponseDto {

    private String code;
    private String description;
    private Integer discountAmount;
    private LocalDate expireAt;
    private FoodCategory foodCategory;
    private Integer minimumSpend;

    public static UserCouponDetailResponseDto from(UserCouponDetail userCouponDetail) {
        UserCoupon userCoupon = userCouponDetail.getUserCoupon();
        Coupon coupon = userCouponDetail.getCoupon();

        return UserCouponDetailResponseDto.builder()
            .code(coupon.getCode())
            .description(coupon.getDescription())
            .discountAmount(coupon.getDiscountAmount())
            .expireAt(userCoupon.getExpireAt())
            .foodCategory(coupon.getFoodCategory())
            .minimumSpend(coupon.getMinimumSpend())
            .build();
    }
}
