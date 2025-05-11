package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_coupon")
public class UserCouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long couponId;

    @Enumerated(EnumType.STRING)
    private UserCouponStatus couponStatus;

    private LocalDate expireAt;

    @Builder
    private UserCouponEntity(
        Long id,
        Long userId,
        Long couponId,
        UserCouponStatus couponStatus,
        LocalDate expireAt
    ) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.couponStatus = couponStatus;
        this.expireAt = expireAt;
    }
}
