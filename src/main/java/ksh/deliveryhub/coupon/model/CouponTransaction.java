package ksh.deliveryhub.coupon.model;

import ksh.deliveryhub.coupon.entity.CouponEventType;
import ksh.deliveryhub.coupon.entity.CouponTransactionEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponTransaction {

    private Long id;
    private CouponEventType eventType;
    private Long userCouponId;
    private Long orderId;
    private Long userId;

    public static CouponTransaction from(
        CouponTransactionEntity couponTransactionEntity
    ) {
        return CouponTransaction.builder()
            .id(couponTransactionEntity.getId())
            .eventType(couponTransactionEntity.getEventType())
            .userCouponId(couponTransactionEntity.getUserCouponId())
            .orderId(couponTransactionEntity.getOrderId())
            .userId(couponTransactionEntity.getUserId())
            .build();
    }

    public CouponTransactionEntity toEntity() {
        return CouponTransactionEntity.builder()
            .id(getId())
            .eventType(getEventType())
            .userCouponId(getUserCouponId())
            .orderId(getOrderId())
            .userId(getUserId())
            .build();
    }
}
