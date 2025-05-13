package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartMenuServiceImpl implements CartMenuService {

    private final CartMenuRepository cartMenuRepository;

    @Override
    public CartMenu addCartMenu(long cartId, CartMenu cartMenu, long storeIdOfNewMenu) {
        Long storeIdOfExistingMenu = cartMenuRepository.findStoreIdOfExistingMenu(cartId);
        if(storeIdOfExistingMenu != null && storeIdOfExistingMenu != storeIdOfNewMenu) {
            throw new CustomException(ErrorCode.CART_MENU_STORE_CONFLICT);
        }

        Optional<CartMenuEntity> optional = cartMenuRepository.findMenuInCart(
            cartId,
            cartMenu.getMenuId(),
            cartMenu.getOptionId()
        );

        if (optional.isPresent()) {
            CartMenuEntity cartMenuEntity = optional.get();
            cartMenuEntity.incrementQuantity(cartMenu.getQuantity());
            return CartMenu.from(cartMenuEntity);
        }

        CartMenuEntity savedEntity = cartMenuRepository.save(cartMenu.toEntity());
        return CartMenu.from(savedEntity);
    }

    @Transactional
    @Override
    public void changeQuantity(long cartId, CartMenu cartMenu) {
        CartMenuEntity cartMenuEntity = cartMenuRepository.findByIdAndCartId(cartMenu.getId(), cartId)
            .orElseThrow(() -> new CustomException(ErrorCode.CART_MENU_NOT_FOUND));

        if(cartMenu.getQuantity() < 1) {
            cartMenuRepository.delete(cartMenuEntity);
            return;
        }

        cartMenuEntity.updateQuantity(cartMenu.getQuantity());
    }

    @Override
    public void deleteCartMenu(long id, long cartId) {
        CartMenuEntity cartMenuEntity = cartMenuRepository.findByIdAndCartId(id, cartId)
            .orElseThrow(() -> new CustomException(ErrorCode.CART_MENU_NOT_FOUND));

        cartMenuRepository.delete(cartMenuEntity);
    }

    @Override
    public void clearCartMenuOfUser(long cartId) {
        cartMenuRepository.deleteByCartId(cartId);
    }

    @Override
    public List<CartMenuDetail> findCartMenusWithDetail(long cartId) {
        return cartMenuRepository.findCartMenusWithDetail(cartId);
    }

    @Override
    public List<CartMenuDetail> checkCartMenuBeforeOrder(long cartId) {
        List<CartMenuDetail> cartMenuDetails = cartMenuRepository.findCartMenusWithDetail(cartId);

        for (CartMenuDetail cartMenuDetail : cartMenuDetails) {
            if(cartMenuDetail.getMenuStatus() != MenuStatus.AVAILABLE) {
                throw new CustomException(ErrorCode.MENU_NOT_AVAILABLE);
            }
        }
        if(cartMenuDetails.isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }

        return cartMenuDetails;
    }
}
