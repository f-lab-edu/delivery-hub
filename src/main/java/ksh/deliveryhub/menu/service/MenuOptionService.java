package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.menu.model.MenuOption;

import java.util.List;

public interface MenuOptionService {

    List<MenuOption> registerMenuOptions(long menuId, List<MenuOption> menuOptions);

    List<MenuOption> updateOptions(List<MenuOption> menuOptions);

    List<MenuOption> deleteMenuOptionsOfMenu(long menuId);
}
