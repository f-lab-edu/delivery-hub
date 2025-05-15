package ksh.deliveryhub.order.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateRequestDto {

    private Long userCouponId;

    private Integer pointToUse;
}
