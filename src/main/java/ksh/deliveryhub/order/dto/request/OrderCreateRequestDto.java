package ksh.deliveryhub.order.dto.request;

import lombok.Getter;

@Getter
public class OrderCreateRequestDto {

    private Long userCouponId;

    private Integer pointToUse;
}
