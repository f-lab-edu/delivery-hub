package ksh.deliveryhub.cart.dto.response;

import ksh.deliveryhub.cart.model.CartMenuDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuResponseDto {

    private long id;
    private int totalPrice;
    private int quantity;
    private String menuName;
    private String menuDescription;
    private int menuPrice;
    private String menuImage;
    private String optionName;
    private Integer optionPrice;

    public static CartMenuResponseDto from(CartMenuDetail cartMenuDetail) {
        int quantity = cartMenuDetail.getQuantity();
        int cartMenuPrice = cartMenuDetail.getMenuPrice() + (cartMenuDetail.getOptionId() != null ? cartMenuDetail.getOptionPrice() : 0);
        int totalPrice = quantity * cartMenuPrice;

        return CartMenuResponseDto.builder()
            .id(cartMenuDetail.getId())
            .totalPrice(totalPrice)
            .quantity(cartMenuDetail.getQuantity())
            .menuName(cartMenuDetail.getMenuName())
            .menuDescription(cartMenuDetail.getMenuDescription())
            .menuPrice(cartMenuDetail.getMenuPrice())
            .menuImage(cartMenuDetail.getImage())
            .optionName(cartMenuDetail.getOptionId() != null ? cartMenuDetail.getOptionName() : null)
            .optionPrice(cartMenuDetail.getOptionId() != null ? cartMenuDetail.getOptionPrice() : null)
            .build();
    }
}
