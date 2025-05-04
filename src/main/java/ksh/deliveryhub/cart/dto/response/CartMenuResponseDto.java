package ksh.deliveryhub.cart.dto.response;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuResponseDto {

    private long id;
    private int quantity;
    private String menuName;
    private String menuDescription;
    private int menuPrice;
    private String menuImage;
    private String optionName;
    private int optionPrice;

    public static CartMenuResponseDto from(CartMenuDetail cartMenuDetail) {
        CartMenu cartMenu = cartMenuDetail.getCartMenu();
        Menu menu = cartMenuDetail.getMenu();
        MenuOption option = cartMenuDetail.getMenuOption();

        return CartMenuResponseDto.builder()
            .id(cartMenu.getId())
            .quantity(cartMenu.getQuantity())
            .menuName(menu.getName())
            .menuDescription(menu.getDescription())
            .menuPrice(menu.getPrice())
            .menuImage(menu.getImage())
            .optionName(option.getName())
            .optionPrice(option.getPrice())
            .build();
    }
}
