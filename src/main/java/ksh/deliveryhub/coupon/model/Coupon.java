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
    private Integer validDays;
    private FoodCategory foodCategory;
    private CouponStatus couponStatus;
    private Integer issueNumberLimit;
    private Integer minimumSpend;

    public static Coupon from(CouponEntity couponEntity) {
        return Coupon.builder()
            .id(couponEntity.getId())
            .code(couponEntity.getCode())
            .description(couponEntity.getDescription())
            .discountAmount(couponEntity.getDiscountAmount())
            .validDays(couponEntity.getValidDays())
            .foodCategory(couponEntity.getFoodCategory())
            .couponStatus(couponEntity.getCouponStatus())
            .issueNumberLimit(couponEntity.getIssueNumberLimit())
            .minimumSpend(couponEntity.getMinimumSpend())
            .build();
    }

    public CouponEntity toEntity(String code) {
        return CouponEntity.builder()
            .id(getId())
            .code(code)
            .description(getDescription())
            .discountAmount(getDiscountAmount())
            .validDays(getValidDays())
            .foodCategory(getFoodCategory())
            .couponStatus(getCouponStatus())
            .issueNumberLimit(getIssueNumberLimit())
            .minimumSpend(getMinimumSpend())
            .build();
    }
}
