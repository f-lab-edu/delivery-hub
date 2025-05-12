package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenuDetail;

import java.util.List;
import java.util.Optional;

public interface CartMenuQueryRepository {

    Optional<CartMenuEntity> findMenuInCart(long cartId, long menuId, Long optionId);

    Long findStoreIdOfExistingMenu(long cartId);

    List<CartMenuDetail> findCartMenusWithDetail(long cartId);
}
