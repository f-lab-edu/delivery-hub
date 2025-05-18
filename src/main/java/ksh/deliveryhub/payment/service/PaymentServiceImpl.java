package ksh.deliveryhub.payment.service;

import ksh.deliveryhub.payment.entity.PaymentEntity;
import ksh.deliveryhub.payment.entity.PaymentMethod;
import ksh.deliveryhub.payment.model.Payment;
import ksh.deliveryhub.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(PaymentMethod paymentMethod, int amount, long orderId) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
            .paymentMethod(paymentMethod)
            .amount(amount)
            .orderId(orderId)
            .build();
        PaymentEntity savedPaymentEntity = paymentRepository.save(paymentEntity);

        return Payment.from(savedPaymentEntity);
    }
}
