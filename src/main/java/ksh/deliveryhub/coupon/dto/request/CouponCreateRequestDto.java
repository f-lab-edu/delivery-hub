package ksh.deliveryhub.coupon.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponCreateRequestDto {

    @NotBlank(message = "쿠폰 상세 설명은 필수입니다.")
    private String description;

    @NotNull(message = "쿠폰 할인 가격은 필수입니다.")
    @Min(value = 1000, message = "쿠폰 할인 가격은 1000원 이상이어야 합니다.")
    private Integer discountAmount;

    @NotNull(message = "쿠폰 유효 일수는 필수입니다.")
    @Min(value = 1, message = "쿠폰 유효 일수는 1일 이상이어야 합니다.")
    private Integer duration;

    private FoodCategory foodCategory;

    @NotNull(message = "쿠폰 발급량은 필수입니다.")
    @PositiveOrZero(message = "쿠폰 발급량은 양수입니다.")
    private Integer issueQuantity;

    @NotNull(message = "쿠폰 최소 사용 금액은 필수입니다.")
    @PositiveOrZero(message = "쿠폰 최소 사용 금액은 양수입니다.")
    private Integer minimumSpend;

    public Coupon toModel() {
        return Coupon.builder()
            .description(description)
            .discountAmount(discountAmount)
            .duration(duration)
            .foodCategory(foodCategory)
            .remainingQuantity(issueQuantity)
            .minimumSpend(minimumSpend)
            .build();
    }
}
