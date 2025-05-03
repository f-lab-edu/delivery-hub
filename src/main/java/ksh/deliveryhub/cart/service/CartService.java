package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.model.Cart;

public interface CartService {

    Cart getUserCart(long userId);
}
