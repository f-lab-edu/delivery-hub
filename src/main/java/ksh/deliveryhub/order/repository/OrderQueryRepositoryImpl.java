package ksh.deliveryhub.order.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.repository.projection.OrderWithStoreInfoProjection;
import ksh.deliveryhub.order.repository.projection.QOrderWithStoreInfoProjection;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.Coordinate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static ksh.deliveryhub.order.entity.QOrderEntity.orderEntity;
import static ksh.deliveryhub.store.entity.QStoreEntity.storeEntity;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final GeometryFactory geometryFactory;

    @Override
    public PageResult<OrderWithStoreInfoProjection> findByStatusAndCurrentLocation(
        OrderStatus status,
        Location location,
        LocalDateTime lastCreatedAt,
        Pageable pageable
    ) {
        int pageSize = pageable.getPageSize();
        int fetchSize = pageSize + 1;

        List<OrderWithStoreInfoProjection> result = queryFactory
            .select(new QOrderWithStoreInfoProjection(orderEntity, storeEntity))
            .from(orderEntity).join(storeEntity).on(orderEntity.storeId.eq(storeEntity.id))
            .where(
                orderEntity.orderStatus.eq(status),
                orderEntity.createdAt.after(lastCreatedAt),
                storeEntity.address.city.eq(location.getCity()),
                storeEntity.address.district.eq(location.getDistrict())
            )
            .limit(fetchSize)
            .orderBy(orderEntity.createdAt.asc())
            .fetch();

        boolean hasNext = result.size() > pageSize;

        List<OrderWithStoreInfoProjection> content = hasNext ? result.subList(0, pageSize) : result;
        return PageResult.of(hasNext, content);
    }

    @Override
    public PageResult<OrderWithStoreInfoProjection> findByStatusAndWithinRadius(
        OrderStatus status,
        Coordinate center,
        double radius,
        Pageable pageable
    ) {
        String centerPointWkt = String.format(
            "POINT (%f %f)",
            center.getLatitude(),
            center.getLongitude()
        );

        BooleanTemplate withinRadius = Expressions.booleanTemplate(
            "ST_Contains(ST_Buffer(ST_GeomFromText({0}, 4326), {1}), {2})",
            Expressions.constant(centerPointWkt),
            Expressions.constant(radius),
            storeEntity.coordinate
        );

        OrderSpecifier<Double> distanceFromStoreAsc = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                storeEntity.coordinate,
                Expressions.constant(centerPointWkt)
            )
            .asc();


        List<OrderWithStoreInfoProjection> result = queryFactory
            .select(new QOrderWithStoreInfoProjection(orderEntity, storeEntity))
            .from(storeEntity)
            .join(orderEntity)
            .on(orderEntity.storeId.eq(storeEntity.id))
            .where(
                orderEntity.orderStatus.eq(OrderStatus.ACCEPTED),
                withinRadius
            )
            .orderBy(
                distanceFromStoreAsc,
                orderEntity.createdAt.asc()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = result.size() > pageable.getPageSize();

        List<OrderWithStoreInfoProjection> pageContent = result;
        if (hasNext) {
            pageContent = result.subList(0, pageable.getPageSize());
        }

        return PageResult.of(hasNext, pageContent);
    }
}
