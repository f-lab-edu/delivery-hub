package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.common.util.CouponCodeUtils;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        String code = CouponCodeUtils.generateCode();
        CouponEntity savedCouponEntity = couponRepository.save(coupon.toEntity(code));

        return Coupon.from(savedCouponEntity);
    }
}
