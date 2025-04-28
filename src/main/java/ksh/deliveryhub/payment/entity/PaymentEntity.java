package ksh.deliveryhub.payment.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Integer amount;

    private Long orderId;

    @Builder
    private PaymentEntity(
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        Integer amount,
        Long orderId
    ) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.orderId = orderId;
    }
}
