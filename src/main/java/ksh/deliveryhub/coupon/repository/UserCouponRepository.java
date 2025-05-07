package ksh.deliveryhub.coupon.repository;

import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long>, UserCouponQueryRepository {
    Optional<UserCouponEntity> findByUserIdAndCouponId(long userId, long couponId);
}
