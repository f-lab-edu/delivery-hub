package ksh.deliveryhub.coupon.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.common.util.CouponCodeUtils;
import ksh.deliveryhub.coupon.dto.response.UserCouponDetailListResponseDto;
import ksh.deliveryhub.coupon.dto.response.UserCouponDetailResponseDto;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.CouponStatus;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static ksh.deliveryhub.coupon.entity.UserCouponStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserCouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserCouponRepository userCouponRepository;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
    }

    @Test
    public void 유저가_코드로_쿠폰을_등록하고_201응답을_받는다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 1, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        //when
        mockMvc.perform(
                post("/users/{userId}/coupons/{code}", 1L, code)
            )
            .andDo(print())
            .andExpect(status().isCreated());

        //then
        CouponEntity issuedCouponEntity = couponRepository.findById(couponEntity.getId()).get();
        assertThat(issuedCouponEntity.getRemainingQuantity()).isEqualTo(0);
        assertThat(issuedCouponEntity.getCouponStatus()).isEqualTo(CouponStatus.INACTIVE);

        UserCouponEntity registeredUserCouponEntity = userCouponRepository
            .findByUserIdAndCouponId(1L, couponEntity.getId()).get();
        assertThat(registeredUserCouponEntity.getCouponId()).isEqualTo(couponEntity.getId());
        assertThat(registeredUserCouponEntity.getCouponStatus()).isEqualTo(ACTIVE);
    }

    @Test
    public void 유저가_쿠폰을_중복_발행하면_400응답을_받는다() throws Exception {
        //given
        String code = CouponCodeUtils.generateCode();
        CouponEntity couponEntity = createCouponEntity(code, 1000, FoodCategory.PIZZA);
        couponRepository.save(couponEntity);

        UserCouponEntity userCouponEntity = createUserCouponEntity(1L, couponEntity.getId());
        userCouponRepository.save(userCouponEntity);

        // when
        MvcResult mvcResult = mockMvc.perform(
                post("/users/{userId}/coupons/{code}", 1L, code)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

        // then
        CustomException ex = (CustomException) mvcResult.getResolvedException();
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_COUPON_ALREADY_REGISTERED);
    }

    @Test
    public void 유저가_사용_가능한_쿠폰을_조회하고_200응답을_받는다() throws Exception{
        //given
        String code1 = CouponCodeUtils.generateCode();
        String code2 = CouponCodeUtils.generateCode();

        CouponEntity couponEntity1 = createCouponEntity(code1, 1, FoodCategory.PIZZA);
        CouponEntity couponEntity2 = createCouponEntity(code2, 1, FoodCategory.PIZZA);
        CouponEntity couponEntity3 = createCouponEntity(code2, 1, FoodCategory.CHICKEN);
        couponRepository.saveAll(List.of(couponEntity1, couponEntity2, couponEntity3));

        UserCouponEntity userCouponEntity1 = createUserCouponEntity(1L, couponEntity1.getId());
        UserCouponEntity userCouponEntity2 = createUserCouponEntity(1L, couponEntity2.getId());
        userCouponRepository.saveAll(List.of(userCouponEntity1, userCouponEntity2));

        //when
        MvcResult mvcResult = mockMvc.perform(
                get("/users/{userId}/coupons", 1L)
                    .param("foodCategory", FoodCategory.PIZZA.name())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        //then
        String json = mvcResult.getResponse().getContentAsString();
        var response = objectMapper.readValue(
                json,
                new TypeReference<SuccessResponseDto<UserCouponDetailListResponseDto>>() {}
            );

        var content = response.getData().getCoupons();
        assertThat(content).hasSize(2)
            .extracting("code", "foodCategory")
            .containsExactlyInAnyOrder(
                tuple(code1, FoodCategory.PIZZA),
                tuple(code2, FoodCategory.PIZZA)
            );
    }

    private static CouponEntity createCouponEntity(String code, int remainingQuantity, FoodCategory foodCategory) {
        return CouponEntity.builder()
            .code(code)
            .description("쿠폰")
            .discountAmount(1000)
            .duration(1)
            .foodCategory(foodCategory)
            .couponStatus(CouponStatus.ACTIVE)
            .remainingQuantity(remainingQuantity)
            .minimumSpend(10000)
            .build();
    }

    private static UserCouponEntity createUserCouponEntity(long userId, long couponId) {
        return UserCouponEntity.builder()
            .userId(userId)
            .couponId(couponId)
            .couponStatus(ACTIVE)
            .build();
    }
}
