package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static ksh.deliveryhub.coupon.entity.UserCouponStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@SuppressWarnings("removal")
class UserCouponServiceImplTest {

    @Autowired
    UserCouponService userCouponService;

    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    CouponRepository couponRepository;

    @MockBean
    Clock clock;

    @AfterEach
    void tearDown() {
        userCouponRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
    }

    @Test
    public void 유저가_쿠폰을_등록한다() throws Exception {
        //given
        Coupon coupon = Coupon.builder()
            .id(1L)
            .duration(10)
            .build();

        Instant fixedInstant = Instant.parse("2025-05-02T00:05:15Z");
        ZoneId zone = ZoneId.of("Asia/Seoul");

        given(clock.instant()).willReturn(fixedInstant);
        given(clock.getZone()).willReturn(zone);

        LocalDate expireAt = LocalDate.of(2025, 5, 12);

        //when
        UserCoupon registeredCoupon = userCouponService.registerCoupon(1L, coupon);

        //then
        UserCouponEntity userCouponEntity = userCouponRepository.findById(registeredCoupon.getId()).get();
        assertThat(userCouponEntity.getUserId()).isEqualTo(1L);
        assertThat(userCouponEntity.getCouponId()).isEqualTo(1L);
        assertThat(userCouponEntity.getCouponStatus()).isEqualTo(ACTIVE);
        assertThat(userCouponEntity.getExpireAt()).isEqualTo(expireAt);
    }

    @Test
    public void 사용자가_이미_등록한_쿠폰을_다시_등록하려_하면_예외가_발생한다() throws Exception {
        //given
        UserCouponEntity userCouponEntity = UserCouponEntity.builder()
            .userId(1L)
            .couponId(1L)
            .build();
        userCouponRepository.save(userCouponEntity);

        Coupon coupon = Coupon.builder()
            .id(1L)
            .duration(10)
            .build();


        //when //then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> userCouponService.registerCoupon(1L, coupon))
            .returns(ErrorCode.USER_COUPON_ALREADY_REGISTERED, CustomException::getErrorCode);
    }

    @Test
    public void 적용_가능한_음식_카테고리를_지정하지_않으면_사용_가능한_모든_쿠폰을_조회한다() throws Exception {
        //given
        CouponEntity couponEntity1 = CouponEntity.builder()
            .foodCategory(FoodCategory.PIZZA)
            .build();

        CouponEntity couponEntity2 = CouponEntity.builder()
            .foodCategory(FoodCategory.CHICKEN)
            .build();
        couponRepository.saveAll(List.of(couponEntity1, couponEntity2));

        UserCouponEntity userCouponEntity1 = UserCouponEntity.builder()
            .userId(1L)
            .couponId(couponEntity1.getId())
            .couponStatus(ACTIVE)
            .build();

        UserCouponEntity userCouponEntity2 = UserCouponEntity.builder()
            .userId(1L)
            .couponId(couponEntity2.getId())
            .couponStatus(ACTIVE)
            .build();
        userCouponRepository.saveAll(List.of(userCouponEntity1, userCouponEntity2));

        //when
        List<UserCouponDetail> userCoupons = userCouponService.findAvailableCouponsWithDetail(1L, null);

        //then
        assertThat(userCoupons).hasSize(2)
            .extracting("userId", "couponId")
            .containsExactlyInAnyOrder(
                tuple(1L, couponEntity1.getId()),
                tuple(1L, couponEntity2.getId())
            );
    }
}
