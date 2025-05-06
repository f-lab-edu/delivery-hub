package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class CouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    private Integer discountAmount;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    private Integer issueNumberLimit;

    private Integer minimumSpend;

    @Builder
    private CouponEntity(
        Long id,
        String code,
        String description,
        Integer discountAmount,
        Integer duration,
        FoodCategory foodCategory,
        CouponStatus couponStatus,
        Integer issueNumberLimit,
        Integer minimumSpend
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountAmount = discountAmount;
        this.duration = duration;
        this.foodCategory = foodCategory;
        this.couponStatus = couponStatus;
        this.issueNumberLimit = issueNumberLimit;
        this.minimumSpend = minimumSpend;
    }
}
