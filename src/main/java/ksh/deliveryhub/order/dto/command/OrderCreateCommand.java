package ksh.deliveryhub.order.dto.command;

import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderCreateCommand {

    private long userId;
    private long storeId;
    private int totalPrice;
    private int discountAmount;
    private int usedPoint;

    public static OrderCreateCommand of(
        UserCouponDetail userCouponDetail,
        List<CartMenuDetail> cartMenuDetails,
        int pointToUse
    ) {
        long userId = userCouponDetail.getUserCoupon().getUserId();
        long storeId = cartMenuDetails.getFirst().getStoreId();
        final int totalPrice = cartMenuDetails.stream()
            .mapToInt(CartMenuDetail::getTotalPrice)
            .sum();
        final int discountAmount = userCouponDetail.getCoupon().getDiscountAmount();


        return OrderCreateCommand.builder()
            .userId(userId)
            .storeId(storeId)
            .totalPrice(totalPrice)
            .discountAmount(discountAmount)
            .usedPoint(pointToUse)
            .build();
    }
}
