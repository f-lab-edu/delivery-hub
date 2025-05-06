package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.CouponTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTransactionRepository extends JpaRepository<CouponTransactionEntity, Long> {
}
