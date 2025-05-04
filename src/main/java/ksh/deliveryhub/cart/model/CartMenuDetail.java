package ksh.deliveryhub.cart.model;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuDetail {

    private CartMenu cartMenu;
    private Menu menu;
    private MenuOption menuOption;

    public static CartMenuDetail of(CartMenu cartMenu, Menu menu, MenuOption menuOption) {
        return CartMenuDetail.builder()
            .cartMenu(cartMenu)
            .menu(menu)
            .menuOption(menuOption)
            .build();
    }
}
