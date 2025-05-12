package ksh.deliveryhub.coupon.dto.response;

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
        return UserCouponDetailResponseDto.builder()
            .code(userCouponDetail.getCode())
            .description(userCouponDetail.getDescription())
            .discountAmount(userCouponDetail.getDiscountAmount())
            .expireAt(userCouponDetail.getExpireAt())
            .foodCategory(userCouponDetail.getFoodCategory())
            .minimumSpend(userCouponDetail.getMinimumSpend())
            .build();
    }
}
