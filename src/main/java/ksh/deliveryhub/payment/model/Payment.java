package ksh.deliveryhub.payment.model;

import ksh.deliveryhub.payment.entity.PaymentEntity;
import ksh.deliveryhub.payment.entity.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Payment {
    private Long id;
    private PaymentMethod paymentMethod;
    private Integer amount;
    private Long orderId;

    public static Payment from(PaymentEntity paymentEntity) {
        return Payment.builder()
            .id(paymentEntity.getId())
            .paymentMethod(paymentEntity.getPaymentMethod())
            .amount(paymentEntity.getAmount())
            .orderId(paymentEntity.getOrderId())
            .build();
    }

    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
            .id(getId())
            .paymentMethod(getPaymentMethod())
            .amount(getAmount())
            .orderId(getOrderId())
            .build();
    }
}
