package ksh.deliveryhub.order.repository;

import ksh.deliveryhub.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
