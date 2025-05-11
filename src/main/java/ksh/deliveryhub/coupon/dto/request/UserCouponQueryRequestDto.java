package ksh.deliveryhub.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCouponQueryRequestDto {

    @NotNull(message = "쿠폰을 적용할 음식 카테고리는 필수입니다.")
    private FoodCategory foodCategory;
}
