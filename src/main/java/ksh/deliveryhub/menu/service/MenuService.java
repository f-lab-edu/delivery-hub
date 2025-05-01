package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.menu.model.Menu;

public interface MenuService {

    Menu registerMenu(Menu menu);

    Menu updateMenu(Menu menu);

    Menu deleteMenu(long id, long storeId);
}
