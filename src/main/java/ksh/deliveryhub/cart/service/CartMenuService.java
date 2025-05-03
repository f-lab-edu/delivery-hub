package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.model.CartMenu;

public interface CartMenuService {

    CartMenu addCartMenu(long cartId, CartMenu cartMenu);
}
