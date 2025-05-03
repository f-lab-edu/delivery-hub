package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartMenuRepository extends JpaRepository<CartMenuEntity, Long>, CartMenuQueryRepository {
}
