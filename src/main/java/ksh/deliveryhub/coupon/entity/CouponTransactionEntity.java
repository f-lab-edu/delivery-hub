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
@Table(name = "coupon_transaction")
public class CouponTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponEventType eventType;

    private Long userCouponId;

    private Long orderId;

    private Long userId;

    @Builder
    private CouponTransactionEntity(
        Long id,
        CouponEventType eventType,
        LocalDateTime eventTime,
        Long userCouponId,
        Long orderId,
        Long userId
    ) {
        this.id = id;
        this.eventType = eventType;
        this.userCouponId = userCouponId;
        this.orderId = orderId;
        this.userId = userId;
    }
}
