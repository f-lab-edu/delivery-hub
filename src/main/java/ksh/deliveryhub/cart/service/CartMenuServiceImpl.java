package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
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
            cartMenuEntity.increaseQuantity(cartMenu.getQuantity());
            return CartMenu.from(cartMenuEntity);
        }

        CartMenuEntity savedEntity = cartMenuRepository.save(cartMenu.toEntity());
        return CartMenu.from(savedEntity);
    }
}
