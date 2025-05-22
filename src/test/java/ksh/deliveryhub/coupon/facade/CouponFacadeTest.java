package ksh.deliveryhub.coupon.facade;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.util.CouponCodeUtils;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import org.jmock.lib.concurrent.Blitzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static ksh.deliveryhub.coupon.entity.CouponStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponFacadeTest {

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponRepository couponRepository;

    @Test
    public void 여러_유저가_동시에_요청했을_때_쿠폰이_모두_소진되면_더_이상_발행하지_않는다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 95, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        Blitzer blitzer = new Blitzer(100, 100);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        AtomicLong userIdSequence = new AtomicLong(1L);

        //when
        blitzer.blitz(
            () -> {
                long userId = userIdSequence.getAndIncrement();
                try {
                    couponFacade.registerUserCoupon(userId, code);
                    successCount.incrementAndGet();
                } catch (CustomException e) {
                    System.out.println("예외 정보 " + e.getErrorCode().name());
                    failureCount.incrementAndGet();
                }
            }
        );

        //then
        System.out.println(userIdSequence.get());
        assertThat(successCount.get()).isEqualTo(95);
        assertThat(failureCount.get()).isEqualTo(5);
    }

    @Test
    public void 한_유저가_쿠폰을_여러번_등록하려_시도하면_최초_1회만_통과한다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 95, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        Blitzer blitzer = new Blitzer(100, 100);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        //when
        blitzer.blitz(
            () -> {
                try {
                    couponFacade.registerUserCoupon(1, code);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                }
            }
        );

        //then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(99);
    }

    private static CouponEntity createCouponEntity(String code, int remainingQuantity, FoodCategory foodCategory) {
        return CouponEntity.builder()
            .code(code)
            .remainingQuantity(remainingQuantity)
            .couponStatus(ACTIVE)
            .duration(30)
            .foodCategory(foodCategory)
            .build();
    }
}
