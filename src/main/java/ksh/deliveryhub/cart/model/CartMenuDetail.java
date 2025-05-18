package ksh.deliveryhub.cart.model;

import ksh.deliveryhub.cart.repository.projection.CartMenuDetailProjection;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuDetail {

    private CartMenu cartMenu;
    private Menu menu;
    private MenuOption menuOption;
    private Store store;

    public static CartMenuDetail from(
        CartMenuDetailProjection projection
    ) {
        MenuOptionEntity menuOptionEntity = projection.getMenuOptionEntity();
        return CartMenuDetail.builder()
            .cartMenu(CartMenu.from(projection.getCartMenuEntity()))
            .menu(Menu.from(projection.getMenuEntity()))
            .menuOption(MenuOption.from(menuOptionEntity))
            .store(Store.from(projection.getStoreEntity()))
            .build();
    }

    public int getTotalPrice() {

        int optionPrice = menuOption != null ? menuOption.getPrice() : 0;
        return (menu.getPrice() + optionPrice) * cartMenu.getQuantity();
    }
}
