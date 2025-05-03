package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartEntity;
import ksh.deliveryhub.cart.entity.CartStatus;
import ksh.deliveryhub.cart.model.Cart;
import ksh.deliveryhub.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart getUserCart(long userId) {
        CartEntity userCartEntity = cartRepository.findByUserId(userId)
            .orElseGet(() -> cartRepository.save(createCartEntity(userId)));

        return Cart.from(userCartEntity);
    }

    private static CartEntity createCartEntity(long userId) {
        LocalDateTime expireAt = LocalDateTime.now().plusHours(1);

        return CartEntity.builder()
            .status(CartStatus.ACTIVE)
            .expireAt(expireAt)
            .userId(userId)
            .build();
    }
}
