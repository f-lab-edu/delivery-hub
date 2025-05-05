package ksh.deliveryhub.menu.facade;

import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.model.MenuWithOptions;
import ksh.deliveryhub.menu.service.MenuOptionService;
import ksh.deliveryhub.menu.service.MenuService;
import ksh.deliveryhub.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuFacade {

    private final MenuService menuService;
    private final MenuOptionService menuOptionService;
    private final StoreService storeService;

    @Transactional
    public MenuWithOptions registerMenu(Menu menu, List<MenuOption> menuOptions) {
        storeService.exists(menu.getStoreId());

        Menu registeredMenu = menuService.registerMenu(menu);
        List<MenuOption> registeredMenuOptions = menuOptionService.registerMenuOptions(registeredMenu.getId(), menuOptions);

        return MenuWithOptions.of(registeredMenu, registeredMenuOptions);
    }

    @Transactional
    public MenuWithOptions updateMenu(Menu menu, List<MenuOption> menuOptions) {
        Menu updatedMenu = menuService.updateMenu(menu);
        List<MenuOption> updatedMenuOptions = menuOptionService.updateOptions(menuOptions);

        return MenuWithOptions.of(updatedMenu, updatedMenuOptions);
    }

    @Transactional
    public MenuWithOptions deleteMenu(long id, long storeId) {
        List<MenuOption> menuOptions = menuOptionService.deleteMenuOptionsOfMenu(id);
        Menu menu = menuService.deleteMenu(id, storeId);

        return MenuWithOptions.of(menu, menuOptions);
    }
}
