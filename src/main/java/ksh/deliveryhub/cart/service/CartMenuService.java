package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.model.CartMenu;

import java.util.List;

public interface CartMenuService {

    CartMenu addCartMenu(long cartId, CartMenu cartMenu);

    void changeQuantity(long cartId, CartMenu cartMenu);

    void deleteCartMenu(long id, long cartId);

    void clearCartMenuOfUser(long cartId);

    List<CartMenu> findCartMenusInCart(long userId);
}
