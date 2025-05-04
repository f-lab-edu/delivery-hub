package ksh.deliveryhub.cart.model;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuDetail {

    private long id;
    private int quantity;
    private String menuName;
    private String menuDescription;
    private int menuPrice;
    private String menuImage;
    private String optionName;
    private int optionPrice;

    public static CartMenuDetail of(CartMenu cartMenu, Menu menu, MenuOption option) {
        return CartMenuDetail.builder()
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
