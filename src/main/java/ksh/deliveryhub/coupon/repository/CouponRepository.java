package ksh.deliveryhub.coupon.repository;

import jakarta.persistence.LockModeType;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponEntity> findByCode(String code);
}
