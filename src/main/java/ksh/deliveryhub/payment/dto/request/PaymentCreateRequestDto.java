package ksh.deliveryhub.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ksh.deliveryhub.payment.entity.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentCreateRequestDto {

    @NotNull(message = "유저 id는 필수입니다.")
    private Long userId;

    @NotNull(message = "사용할 쿠폰의 id는 필수입니다.")
    private Long userCouponId;

    @NotNull(message = "사용할 포인트 양은 필수입니다.")
    @Positive(message = "사용할 포인트 양은 양수입니다.")
    private Integer pointToUse;

    @NotNull(message = "결제 방법은 필수입니다.")
    private PaymentMethod paymentMethod;
}
