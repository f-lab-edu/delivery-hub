package ksh.deliveryhub.menu.facade;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.service.MenuService;
import ksh.deliveryhub.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuFacade {

    private final MenuService menuService;
    private final StoreService storeService;

    public Menu registerMenu(Menu menu) {
        storeService.exists(menu.getStoreId());
        return menuService.registerMenu(menu);
    }

    public Menu updateMenu(Menu menu) {
        return menuService.updateMenu(menu);
    }

    public Menu deleteMenu(long id, long storeId) {
        return menuService.deleteMenu(id, storeId);
    }
}
