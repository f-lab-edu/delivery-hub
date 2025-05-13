package ksh.deliveryhub.order.repository;

import ksh.deliveryhub.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
