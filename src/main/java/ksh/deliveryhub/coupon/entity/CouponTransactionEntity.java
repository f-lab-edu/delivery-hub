package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponEventType couponEventType;

    private LocalDateTime eventTime;

    private Long couponId;

    private Long orderId;

    private CouponTransactionEntity(
        CouponEventType couponEventType,
        LocalDateTime eventTime,
        Long couponId,
        Long orderId
    ) {
        this.couponEventType = couponEventType;
        this.eventTime = eventTime;
        this.couponId = couponId;
        this.orderId = orderId;
    }
}
