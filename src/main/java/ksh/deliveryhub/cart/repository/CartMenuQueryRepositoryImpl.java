package ksh.deliveryhub.cart.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.model.QCartMenuDetail;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static ksh.deliveryhub.cart.entity.QCartMenuEntity.cartMenuEntity;
import static ksh.deliveryhub.menu.entity.QMenuEntity.menuEntity;
import static ksh.deliveryhub.menu.entity.QMenuOptionEntity.menuOptionEntity;

@RequiredArgsConstructor
public class CartMenuQueryRepositoryImpl implements CartMenuQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<CartMenuEntity> findMenuInCart(long cartId, long menuId, Long optionId) {
        BooleanExpression optionIdEqual = optionId == null ? cartMenuEntity.optionId.isNull() : cartMenuEntity.optionId.eq(optionId);

        CartMenuEntity entity = queryFactory
            .select(cartMenuEntity)
            .from(cartMenuEntity)
            .where(
                cartMenuEntity.cartId.eq(cartId),
                cartMenuEntity.menuId.eq(menuId),
                optionIdEqual
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

    @Override
    public List<CartMenuDetail> findCartMenusWithDetail(long cartId) {
        return queryFactory
            .select(new QCartMenuDetail(
                cartMenuEntity.id,
                cartMenuEntity.quantity,

                menuEntity.id,
                menuEntity.name,
                menuEntity.description,
                menuEntity.menuStatus,
                menuEntity.price,
                menuEntity.image,
                menuEntity.storeId,

                menuOptionEntity.id,
                menuOptionEntity.name,
                menuOptionEntity.price
            ))
            .from(cartMenuEntity)
            .join(menuEntity).on(cartMenuEntity.menuId.eq(menuEntity.id))
            .leftJoin(menuOptionEntity).on(cartMenuEntity.optionId.eq(menuOptionEntity.id))
            .where(cartMenuEntity.cartId.eq(cartId))
            .fetch();
    }
}
