package ksh.deliveryhub.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.menu.entity.QMenuEntity;
import ksh.deliveryhub.order.entity.QOrderItemEntity;
import ksh.deliveryhub.order.repository.projection.OrderItemWithMenuProjection;
import ksh.deliveryhub.order.repository.projection.QOrderItemWithMenuProjection;
import ksh.deliveryhub.order.repository.projection.QOrderWithStoreInfoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ksh.deliveryhub.menu.entity.QMenuEntity.*;
import static ksh.deliveryhub.order.entity.QOrderItemEntity.*;

@Repository
@RequiredArgsConstructor
public class OrderItemQueryRepositoryImpl implements OrderItemQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderItemWithMenuProjection> getOrderItemWithMenuIn(List<Long> orderIds) {
        return queryFactory
            .select(new QOrderItemWithMenuProjection(orderItemEntity, menuEntity))
            .from(orderItemEntity).join(menuEntity).on(orderItemEntity.menuId.eq(menuEntity.id))
            .where(orderItemEntity.orderId.in(orderIds))
            .fetch();
    }
}
