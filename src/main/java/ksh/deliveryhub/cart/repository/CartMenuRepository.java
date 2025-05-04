package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartMenuRepository extends JpaRepository<CartMenuEntity, Long>, CartMenuQueryRepository {

    Optional<CartMenuEntity> findByIdAndCartId(long id, long cartId);
}
