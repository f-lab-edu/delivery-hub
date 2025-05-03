package ksh.deliveryhub.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.entity.QCartMenuEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CartMenuQueryRepositoryImpl implements CartMenuQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CartMenuEntity> findMenuInCart(long cartId, long menuId, long optionId) {
        CartMenuEntity cartMenuEntity = queryFactory
            .select(QCartMenuEntity.cartMenuEntity)
            .from(QCartMenuEntity.cartMenuEntity)
            .where(
                QCartMenuEntity.cartMenuEntity.cartId.eq(cartId),
                QCartMenuEntity.cartMenuEntity.menuId.eq(menuId),
                QCartMenuEntity.cartMenuEntity.optionId.eq(optionId)
            )
            .fetchOne();

        return Optional.ofNullable(cartMenuEntity);
    }
}
