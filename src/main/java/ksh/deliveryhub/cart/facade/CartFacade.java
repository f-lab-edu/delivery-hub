package ksh.deliveryhub.cart.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartFacade {

    private final CartService cartService;
    private final CartMenuService cartMenuService;
    private final MenuService menuService;

    public CartMenu addMenuToCart(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        menuService.checkAvailability(cartMenu.getMenuId());
        return cartMenuService.addCartMenu(cart.getId(), cartMenu);
    }
}
