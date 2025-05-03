package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartMenuEntity;

import java.util.Optional;

public interface CartMenuQueryRepository {

    Optional<CartMenuEntity> findMenuInCart(long cartId, long menuId, long optionId);
}
