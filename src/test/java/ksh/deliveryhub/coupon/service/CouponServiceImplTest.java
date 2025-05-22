package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.common.util.CouponCodeUtils;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ksh.deliveryhub.coupon.entity.CouponStatus.ACTIVE;
import static ksh.deliveryhub.coupon.entity.CouponStatus.INACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponServiceImplTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAllInBatch();
    }

    @Test
    public void 요청한_코드로_쿠폰을_발행하면_남은_수량이_1_감소한다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 10, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        //when
        couponService.issueCoupon(code);

        //then
        CouponEntity decreasedCouponEntity = couponRepository.findById(couponEntity.getId()).get();
        assertThat(decreasedCouponEntity.getRemainingQuantity()).isEqualTo(9);
    }

    @Test
    public void 쿠폰을_발행하고_남은_수량이_0이_되면_쿠폰을_비활성화한다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 1, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        //when
        couponService.issueCoupon(code);

        //then
        CouponEntity decreasedCouponEntity = couponRepository.findById(couponEntity.getId()).get();
        assertThat(decreasedCouponEntity.getCouponStatus()).isEqualTo(INACTIVE);
    }

    private static CouponEntity createCouponEntity(String code, int remainingQuantity, FoodCategory foodCategory) {
        return CouponEntity.builder()
            .code(code)
            .remainingQuantity(remainingQuantity)
            .couponStatus(ACTIVE)
            .foodCategory(foodCategory)
            .build();
    }
}
