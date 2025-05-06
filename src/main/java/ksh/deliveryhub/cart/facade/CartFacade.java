package ksh.deliveryhub.cart.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.service.MenuOptionService;
import ksh.deliveryhub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartFacade {

    private final CartService cartService;
    private final CartMenuService cartMenuService;
    private final MenuService menuService;
    private final MenuOptionService menuOptionService;

    public CartMenu addMenuToCart(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        Menu selectedMenu = menuService.getAvailableMenu(cartMenu.getMenuId());
        menuOptionService.getOptionInMenu(cartMenu.getOptionId(), cartMenu.getMenuId());

        return cartMenuService.addCartMenu(cart.getId(), cartMenu, selectedMenu.getStoreId());
    }

    public void changeQuantity(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.changeQuantity(cart.getId(), cartMenu);
    }

    public void deleteMenuInCart(long userId, long cartMenuId) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.deleteCartMenu(cartMenuId, cart.getId());
    }

    public void clearCart(long userId) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.clearCartMenuOfUser(cart.getId());
    }

    public List<CartMenuDetail> getUserCartMenuDetails(long userId) {
        Cart cart = cartService.getUserCart(userId);
        List<CartMenu> cartMenus = cartMenuService.findCartMenusInCart(cart.getId());
        List<Menu> menusInCart = menuService.findMenusInCart(cartMenus);
        List<MenuOption> optionsOfCartMenu = menuOptionService.findOptionsOfCartMenu(cartMenus);

        Map<Long, Menu> menuMap = menusInCart.stream()
            .collect(Collectors.toMap(Menu::getId, Function.identity()));

        Map<Long, MenuOption> optionMap = optionsOfCartMenu.stream()
            .collect(Collectors.toMap(MenuOption::getId, Function.identity()));

        return cartMenus.stream()
            .map(cm -> CartMenuDetail.of(
                cm,
                menuMap.get(cm.getMenuId()),
                optionMap.get(cm.getOptionId())
            ))
            .toList();
    }
}
