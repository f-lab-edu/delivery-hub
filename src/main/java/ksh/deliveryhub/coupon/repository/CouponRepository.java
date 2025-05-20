package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    Optional<CouponEntity> findByCode(String code);
}
