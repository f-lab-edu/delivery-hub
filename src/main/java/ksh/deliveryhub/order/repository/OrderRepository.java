package ksh.deliveryhub.order.repository;

import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByIdAndUserIdAndOrderStatus(long id, long userId, OrderStatus status);
}
