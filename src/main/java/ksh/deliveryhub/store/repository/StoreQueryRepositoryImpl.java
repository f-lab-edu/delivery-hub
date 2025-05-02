package ksh.deliveryhub.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.entity.StoreStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static ksh.deliveryhub.store.entity.QStoreEntity.storeEntity;

@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreEntity> findOpenStores(String address, FoodCategory foodCategory, Pageable pageable) {
        BooleanExpression predicate = storeEntity.address.eq(address)
            .and(storeEntity.foodCategory.eq(foodCategory))
            .and(storeEntity.status.eq(StoreStatus.OPEN));

        List<StoreEntity> storeEntities = queryFactory
            .select(storeEntity)
            .from(storeEntity)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalElements = queryFactory
            .select(storeEntity.count())
            .from(storeEntity)
            .where(predicate)
            .fetchFirst();

        return new PageImpl<>(storeEntities, pageable, totalElements);
    }
}
