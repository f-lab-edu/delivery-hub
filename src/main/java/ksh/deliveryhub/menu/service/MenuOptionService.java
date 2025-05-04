package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.menu.model.MenuOption;

import java.util.List;

public interface MenuOptionService {

    List<MenuOption> registerMenuOptions(long menuId, List<MenuOption> menuOptions);

    List<MenuOption> updateOptions(List<MenuOption> menuOptions);

    List<MenuOption> deleteMenuOptionsOfMenu(long menuId);

    MenuOption getOptionIsInMenu(long id, long menuId);

    List<MenuOption> findOptionsOfCartMenu(List<CartMenu> cartMenus);
}
