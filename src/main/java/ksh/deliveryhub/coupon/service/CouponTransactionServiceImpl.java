package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.entity.CouponEventType;
import ksh.deliveryhub.coupon.entity.CouponTransactionEntity;
import ksh.deliveryhub.coupon.model.CouponTransaction;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.repository.CouponTransactionRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class CouponTransactionServiceImpl implements CouponTransactionService {

    private final CouponTransactionRepository couponTransactionRepository;

    @Override
    public CouponTransaction saveIssueTransaction(UserCoupon userCoupon) {
        CouponTransactionEntity couponTransactionEntity = CouponTransactionEntity.builder()
            .userCouponId(userCoupon.getId())
            .eventType(CouponEventType.ISSUED)
            .build();
        couponTransactionRepository.save(couponTransactionEntity);

        return CouponTransaction.from(couponTransactionEntity);
    }
}
