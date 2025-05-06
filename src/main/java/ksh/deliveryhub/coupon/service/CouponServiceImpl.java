package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.common.util.CouponCodeUtils;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.CouponStatus;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Builder
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        String code = CouponCodeUtils.generateCode();
        CouponEntity savedCouponEntity = couponRepository.save(coupon.toEntity(code));

        return Coupon.from(savedCouponEntity);
    }

    @Transactional
    @Override
    public Coupon issueCoupon(String code) {
        CouponEntity couponEntity = couponRepository.findByCode(code)
            .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND, List.of(code)));

        if(couponEntity.getCouponStatus() == CouponStatus.INACTIVE) {
            throw new CustomException(ErrorCode.COUPON_OUT_OF_STOCK);
        }

        couponEntity.decreaseRemainingQuantity();

        return Coupon.from(couponRepository.save(couponEntity));
    }
}
