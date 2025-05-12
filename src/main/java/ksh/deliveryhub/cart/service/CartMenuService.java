package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;

import java.util.List;

public interface CartMenuService {

    CartMenu addCartMenu(long cartId, CartMenu cartMenu, long storeIdOfNewMenu);

    void changeQuantity(long cartId, CartMenu cartMenu);

    void deleteCartMenu(long id, long cartId);

    void clearCartMenuOfUser(long cartId);

    List<CartMenuDetail> findCartMenusWithDetail(long cartId);
}
