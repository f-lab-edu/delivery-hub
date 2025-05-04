package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.menu.model.Menu;

import java.util.List;

public interface MenuService {

    Menu registerMenu(Menu menu);

    Menu updateMenu(Menu menu);

    Menu deleteMenu(long id, long storeId);

    Menu getAvailableMenu(long menuId);

    List<Menu> findMenusInCart(List<CartMenu> cartMenus);
}
