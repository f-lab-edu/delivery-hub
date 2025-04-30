package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private Integer validDays;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    private Integer issueNumberLimit;

    private Integer minimumSpend;

    @Builder
    private CouponEntity(
        String code,
        String description,
        Integer discountAmount,
        Integer validDays,
        FoodCategory foodCategory,
        Integer issueNumberLimit,
        Integer minimumSpend
    ) {
        this.code = code;
        this.description = description;
        this.discountAmount = discountAmount;
        this.validDays = validDays;
        this.foodCategory = foodCategory;
        this.issueNumberLimit = issueNumberLimit;
        this.minimumSpend = minimumSpend;
    }
}
