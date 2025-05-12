package ksh.deliveryhub.coupon.model;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.coupon.entity.CouponStatus;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class UserCouponDetail {

    private Long id;
    private Long userId;
    private UserCouponStatus userCouponStatus;
    private LocalDate expireAt;

    private Long couponId;
    private String code;
    private String description;
    private Integer discountAmount;
    private Integer duration;
    private FoodCategory foodCategory;
    private CouponStatus couponStatus;
    private Integer remainingQuantity;
    private Integer minimumSpend;
}
