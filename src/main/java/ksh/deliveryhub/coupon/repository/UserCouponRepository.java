package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long> {
}
