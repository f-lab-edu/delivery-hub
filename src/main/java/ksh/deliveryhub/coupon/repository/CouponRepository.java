package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}
