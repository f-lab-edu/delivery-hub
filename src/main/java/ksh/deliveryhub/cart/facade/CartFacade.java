package ksh.deliveryhub.cart.facade;

import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.service.CartMenuService;
import ksh.deliveryhub.cart.service.CartService;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.service.MenuOptionService;
import ksh.deliveryhub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartFacade {

    private final CartService cartService;
    private final CartMenuService cartMenuService;
    private final MenuService menuService;
    private final MenuOptionService menuOptionService;

    @Transactional
    public CartMenu addMenuToCart(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        Menu selectedMenu = menuService.getAvailableMenu(cartMenu.getMenuId());
        if(cartMenu.getOptionId() != null){
            menuOptionService.getOptionInMenu(cartMenu.getOptionId(), cartMenu.getMenuId());
        }

        return cartMenuService.addCartMenu(cart.getId(), cartMenu, selectedMenu.getStoreId());
    }

    @Transactional
    public void changeQuantity(long userId, CartMenu cartMenu) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.changeQuantity(cart.getId(), cartMenu);
    }

    @Transactional
    public void deleteMenuInCart(long userId, long cartMenuId) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.deleteCartMenu(cartMenuId, cart.getId());
    }

    @Transactional
    public void clearCart(long userId) {
        Cart cart = cartService.getUserCart(userId);
        cartMenuService.clearCartMenuOfUser(cart.getId());
    }

    @Transactional(readOnly = true)
    public List<CartMenuDetail> findUserCartMenuDetails(long userId) {
        Cart cart = cartService.getUserCart(userId);
        return cartMenuService.findCartMenusWithDetail(cart.getId());
    }
}
