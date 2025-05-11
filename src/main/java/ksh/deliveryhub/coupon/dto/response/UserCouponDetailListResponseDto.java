package ksh.deliveryhub.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserCouponDetailListResponseDto {

    private final List<UserCouponDetailResponseDto> coupons;

    public static UserCouponDetailListResponseDto of(List<UserCouponDetailResponseDto> coupons) {
        return new UserCouponDetailListResponseDto(coupons);
    }
}
