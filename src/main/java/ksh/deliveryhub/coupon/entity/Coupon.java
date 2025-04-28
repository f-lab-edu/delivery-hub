package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    private Integer discountAmount;

    private LocalDateTime expireDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    private Long userId;

    @Builder
    private Coupon(
        String code,
        String description,
        Integer discountAmount,
        LocalDateTime expireDate,
        CouponStatus couponStatus,
        Long userId
    ) {
        this.code = code;
        this.description = description;
        this.discountAmount = discountAmount;
        this.expireDate = expireDate;
        this.couponStatus = couponStatus;
        this.userId = userId;
    }
}
