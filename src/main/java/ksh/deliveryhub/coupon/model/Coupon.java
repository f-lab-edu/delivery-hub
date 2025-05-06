package ksh.deliveryhub.coupon.model;

import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.CouponStatus;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Coupon {

    private Long id;
    private String code;
    private String description;
    private Integer discountAmount;
    private Integer duration;
    private FoodCategory foodCategory;
    private CouponStatus couponStatus;
    private Integer remainingQuantity;
    private Integer minimumSpend;

    public static Coupon from(CouponEntity couponEntity) {
        return Coupon.builder()
            .id(couponEntity.getId())
            .code(couponEntity.getCode())
            .description(couponEntity.getDescription())
            .discountAmount(couponEntity.getDiscountAmount())
            .duration(couponEntity.getDuration())
            .foodCategory(couponEntity.getFoodCategory())
            .couponStatus(couponEntity.getCouponStatus())
            .remainingQuantity(couponEntity.getRemainingQuantity())
            .minimumSpend(couponEntity.getMinimumSpend())
            .build();
    }

    public CouponEntity toEntity(String code) {
        return CouponEntity.builder()
            .id(getId())
            .code(code)
            .description(getDescription())
            .discountAmount(getDiscountAmount())
            .duration(getDuration())
            .foodCategory(getFoodCategory())
            .couponStatus(getCouponStatus())
            .remainingQuantity(getRemainingQuantity())
            .minimumSpend(getMinimumSpend())
            .build();
    }
}
