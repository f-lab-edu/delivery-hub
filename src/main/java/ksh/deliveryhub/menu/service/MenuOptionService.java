package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.menu.model.MenuOption;

import java.util.List;

public interface MenuOptionService {

    List<MenuOption> registerMenuOptions(long menuId, List<MenuOption> menuOptions);
}
