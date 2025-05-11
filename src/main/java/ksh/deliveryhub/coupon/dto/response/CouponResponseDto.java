package ksh.deliveryhub.coupon.dto.response;

import ksh.deliveryhub.coupon.entity.CouponStatus;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponseDto {

    private String code;
    private String description;
    private Integer discountAmount;
    private Integer duration;
    private FoodCategory foodCategory;
    private CouponStatus couponStatus;
    private Integer remainingQuantity;
    private Integer minimumSpend;

    public static CouponResponseDto from(ksh.deliveryhub.coupon.model.Coupon coupon) {
        return CouponResponseDto.builder()
            .code(coupon.getCode())
            .description(coupon.getDescription())
            .discountAmount(coupon.getDiscountAmount())
            .duration(coupon.getDuration())
            .foodCategory(coupon.getFoodCategory())
            .couponStatus(coupon.getCouponStatus())
            .remainingQuantity(coupon.getRemainingQuantity())
            .minimumSpend(coupon.getMinimumSpend())
            .build();
    }
}
