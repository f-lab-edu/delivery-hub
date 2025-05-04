package ksh.deliveryhub.cart.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.menu.service.MenuOptionService;
import ksh.deliveryhub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

    private final CartService cartService;
    private final CartMenuService cartMenuService;
    private final MenuService menuService;
    private final MenuOptionService menuOptionService;

    public CartMenu addMenuToCart(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        menuService.getAvailableMenu(cartMenu.getMenuId());
        menuOptionService.getOptionIsInMenu(cartMenu.getOptionId(), cartMenu.getMenuId());

        return cartMenuService.addCartMenu(cart.getId(), cartMenu);
    }

    public void changeQuantity(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.changeQuantity(cart.getId(), cartMenu);
    }

    public void deleteMenuInCart(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.deleteCartMenu(cartMenu.getId(), cart.getId());
    }

    public void clearCart(long userId) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.clearCartMenuOfUser(cart.getId());
    }
}
