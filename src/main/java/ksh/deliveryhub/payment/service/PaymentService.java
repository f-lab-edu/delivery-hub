package ksh.deliveryhub.payment.service;

import ksh.deliveryhub.payment.entity.PaymentMethod;
import ksh.deliveryhub.payment.model.Payment;

public interface PaymentService {

    Payment savePayment(PaymentMethod paymentMethod, int amount, long orderId);
}
