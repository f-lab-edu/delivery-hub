package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartMenuServiceImpl implements CartMenuService {

    private final CartMenuRepository cartMenuRepository;

    @Override
    public CartMenu addCartMenu(long cartId, CartMenu cartMenu) {
        Optional<CartMenuEntity> optional = cartMenuRepository.findMenuInCart(
            cartId,
            cartMenu.getMenuId(),
            cartMenu.getOptionId()
        );

        if (optional.isPresent()) {
            CartMenuEntity cartMenuEntity = optional.get();
            cartMenuEntity.updateQuantity(cartMenu.getQuantity());
            return CartMenu.from(cartMenuEntity);
        }

        CartMenuEntity savedEntity = cartMenuRepository.save(cartMenu.toEntity());
        return CartMenu.from(savedEntity);
    }

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
}
