package ksh.deliveryhub.coupon.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class UserCouponDetailProjection {

    private UserCouponEntity userCouponEntity;
    private CouponEntity couponEntity;
}
