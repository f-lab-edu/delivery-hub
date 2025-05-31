package ksh.deliveryhub.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.repository.projection.OrderWithStoreInfoProjection;
import ksh.deliveryhub.order.repository.projection.QOrderWithStoreInfoProjection;
import ksh.deliveryhub.rider.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ksh.deliveryhub.order.entity.QOrderEntity.orderEntity;
import static ksh.deliveryhub.store.entity.QStoreEntity.storeEntity;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public PageResult<OrderWithStoreInfoProjection> findByStatusAndCurrentLocation(
        OrderStatus status,
        Location location,
        Pageable pageable
    ) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        int fetchSize = pageSize + 1;

        List<OrderWithStoreInfoProjection> result = queryFactory
            .select(new QOrderWithStoreInfoProjection(orderEntity, storeEntity))
            .from(orderEntity).join(storeEntity).on(orderEntity.storeId.eq(storeEntity.id))
            .where(
                orderEntity.orderStatus.eq(status),
                storeEntity.address.city.eq(location.getCity()),
                storeEntity.address.district.eq(location.getDistrict())
            )
            .offset(offset)
            .limit(fetchSize)
            .orderBy(orderEntity.createdAt.asc())
            .fetch();

        boolean hasNext = result.size() > pageSize;

        List<OrderWithStoreInfoProjection> content = hasNext ? result.subList(0, pageSize) : result;
        return PageResult.of(hasNext, content);
    }
}
