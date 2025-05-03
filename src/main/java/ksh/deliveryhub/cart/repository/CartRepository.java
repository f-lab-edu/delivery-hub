package ksh.deliveryhub.cart.repository;

import ksh.deliveryhub.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(long userId);
}
