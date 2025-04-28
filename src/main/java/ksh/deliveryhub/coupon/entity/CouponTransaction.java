package ksh.deliveryhub.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponEventType couponEventType;

    private LocalDateTime eventTime;

    private Long couponId;

    private Long orderId;

    private CouponTransaction(
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
