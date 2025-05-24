package ksh.deliveryhub.coupon.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCouponRegisterEvent {

    private long couponId;
    private long userId;
    private int duration;
}
