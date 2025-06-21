package ksh.deliveryhub.order.repository;

import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.repository.projection.OrderWithStoreInfoProjection;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.Coordinate;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderQueryRepository {

    PageResult<OrderWithStoreInfoProjection> findByStatusAndCurrentLocation(OrderStatus status, Location location, LocalDateTime lastCreatedAt, Pageable pageable);

    PageResult<OrderWithStoreInfoProjection> findByStatusAndWithinRadius(OrderStatus status, Coordinate center, double distance, Pageable pageable);
}
