package ksh.deliveryhub.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static ksh.deliveryhub.cart.entity.QCartMenuEntity.cartMenuEntity;
import static ksh.deliveryhub.menu.entity.QMenuEntity.menuEntity;

@RequiredArgsConstructor
public class CartMenuQueryRepositoryImpl implements CartMenuQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CartMenuEntity> findMenuInCart(long cartId, long menuId, long optionId) {
        CartMenuEntity entity = queryFactory
            .select(cartMenuEntity)
            .from(cartMenuEntity)
            .where(
                cartMenuEntity.cartId.eq(cartId),
                cartMenuEntity.menuId.eq(menuId),
                cartMenuEntity.optionId.eq(optionId)
            )
            .fetchOne();

        return Optional.ofNullable(entity);
    }

    @Override
    public Long findStoreIdOfExistingMenu(long cartId) {
        return queryFactory
            .select(menuEntity.storeId)
            .from(cartMenuEntity)
            .join(menuEntity)
            .on(cartMenuEntity.menuId.eq(menuEntity.id))
            .where(cartMenuEntity.cartId.eq(cartId))
            .limit(1)
            .fetchOne();
    }
}
