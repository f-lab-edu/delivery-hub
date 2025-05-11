package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartMenuRepository extends JpaRepository<CartMenuEntity, Long>, CartMenuQueryRepository {

    List<CartMenuEntity> findByCartId(long cartId);

    Optional<CartMenuEntity> findByIdAndCartId(long id, long cartId);

    void deleteByCartId(long cartId);
}
