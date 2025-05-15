package ksh.deliveryhub.payment.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.payment.dto.request.PaymentCreateRequestDto;
import ksh.deliveryhub.payment.facade.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping("/orders/{orderId}/payments")
    public ResponseEntity<Void> pay(
        @PathVariable("orderId") long orderId,
        @Valid @RequestBody PaymentCreateRequestDto request
    ) {
        paymentFacade.processPayment(
            request.getUserId(),
            orderId,
            request.getUserCouponId(),
            request.getPointToUse(),
            request.getPaymentMethod()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
