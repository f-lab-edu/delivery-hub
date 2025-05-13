package ksh.deliveryhub.order.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.repository.CouponRepository;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import ksh.deliveryhub.menu.repository.MenuRepository;
import ksh.deliveryhub.order.dto.request.OrderCreateRequestDto;
import ksh.deliveryhub.order.dto.response.OrderCreateResponseDto;
import ksh.deliveryhub.point.entity.UserPointEntity;
import ksh.deliveryhub.point.repository.UserPointRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CartMenuRepository cartMenuRepository;
    @Autowired
    UserCouponRepository userCouponRepository;
    @Autowired
    UserPointRepository userPointRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuOptionRepository menuOptionRepository;
    @Autowired
    CouponRepository couponRepository;

    @Test
    void 사용할_쿠폰과_포인트_정보를_포함해_주문을_생성하고_주문_정보와_201_응답을_반환한다() throws Exception {
        // given
        StoreEntity store = createStore();
        storeRepository.save(store);

        MenuEntity menu1 = createMenu("치즈 피자", 10000, store.getId());
        MenuEntity menu2 = createMenu("페퍼로니 피자", 15000, store.getId());
        menuRepository.saveAll(List.of(menu1, menu2));

        MenuOptionEntity option = createOption("치즈 추가", 500, menu1.getId());
        menuOptionRepository.save(option);

        CartMenuEntity cart1 = createCartMenu(1L, menu1.getId(), option.getId(), 2);
        CartMenuEntity cart2 = createCartMenu(1L, menu2.getId(), null, 1);
        cartMenuRepository.saveAll(List.of(cart1, cart2));

        CouponEntity coupon = createCoupon(FoodCategory.PIZZA, 3000);
        couponRepository.save(coupon);

        UserCouponEntity userCoupon = createUserCoupon(1L, coupon.getId(), UserCouponStatus.ACTIVE);
        userCouponRepository.save(userCoupon);

        UserPointEntity userPoint = createUserPoint(1L, 10000);
        userPointRepository.save(userPoint);

        OrderCreateRequestDto request = createOrderCreateRequestDto(userCoupon.getId(), 2000);

        //when
        String json = mockMvc.perform(
                post("/users/{userId}/orders", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        //then
        var response = readResponseDto(json, OrderCreateResponseDto.class);
        OrderCreateResponseDto responseDto = response.getData();
        assertThat(responseDto.getId()).isNotZero();
        assertThat(responseDto.getTotalPrice()).isEqualTo(31000);
    }

    @Test
    void 쿠폰과_포인트를_사용하지_않아도_주문을_생성하고_201_응답을_받을_수_있다() throws Exception {
        // given
        StoreEntity store = createStore();
        storeRepository.save(store);

        MenuEntity menu1 = createMenu("치즈 피자", 10000, store.getId());
        MenuEntity menu2 = createMenu("페퍼로니 피자", 15000, store.getId());
        menuRepository.saveAll(List.of(menu1, menu2));

        MenuOptionEntity option = createOption("치즈 추가", 500, menu1.getId());
        menuOptionRepository.save(option);

        CartMenuEntity cart1 = createCartMenu(1L, menu1.getId(), option.getId(), 2);
        CartMenuEntity cart2 = createCartMenu(1L, menu2.getId(), null, 1);
        cartMenuRepository.saveAll(List.of(cart1, cart2));

        CouponEntity coupon = createCoupon(FoodCategory.PIZZA, 3000);
        couponRepository.save(coupon);

        UserCouponEntity userCoupon = createUserCoupon(1L, coupon.getId(), UserCouponStatus.ACTIVE);
        userCouponRepository.save(userCoupon);

        UserPointEntity userPoint = createUserPoint(1L, 10000);
        userPointRepository.save(userPoint);

        OrderCreateRequestDto request = createOrderCreateRequestDto(null, null);

        //when
        String json = mockMvc.perform(
                post("/users/{userId}/orders", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        //then
        var response = readResponseDto(json, OrderCreateResponseDto.class);
        OrderCreateResponseDto responseDto = response.getData();
        assertThat(responseDto.getId()).isNotZero();
        assertThat(responseDto.getTotalPrice()).isEqualTo(36000);
    }

    private static StoreEntity createStore() {
        return StoreEntity.builder()
            .foodCategory(FoodCategory.PIZZA)
            .build();
    }

    private static MenuEntity createMenu(String name, int price, long storeId) {
        return MenuEntity.builder()
            .name(name)
            .description("설명")
            .menuStatus(MenuStatus.AVAILABLE)
            .price(price)
            .image("https://example.com/image.png")
            .storeId(storeId)
            .build();
    }

    private static MenuOptionEntity createOption(String name, int price, long menuId) {
        return MenuOptionEntity.builder()
            .name(name)
            .price(price)
            .menuId(menuId)
            .build();
    }

    private static CartMenuEntity createCartMenu(long cartId, long menuId, Long optionId, int qty) {
        return CartMenuEntity.builder()
            .cartId(cartId)
            .menuId(menuId)
            .optionId(optionId)
            .quantity(qty)
            .build();
    }

    private static CouponEntity createCoupon(FoodCategory category, int discount) {
        return CouponEntity.builder()
            .foodCategory(category)
            .discountAmount(discount)
            .build();
    }

    private static UserCouponEntity createUserCoupon(long userId, long couponId, UserCouponStatus status) {
        return UserCouponEntity.builder()
            .userId(userId)
            .couponId(couponId)
            .couponStatus(status)
            .build();
    }

    private static UserPointEntity createUserPoint(long userId, int balance) {
        return UserPointEntity.builder()
            .userId(userId)
            .balance(balance)
            .build();
    }

    private static OrderCreateRequestDto createOrderCreateRequestDto(Long userCouponId, Integer pointToUse) {
        return OrderCreateRequestDto.builder()
            .userCouponId(userCouponId)
            .pointToUse(pointToUse)
            .build();
    }

    private <T> SuccessResponseDto<T> readResponseDto(
        String json,
        Class<T> responseDtoClass
    ) throws JsonProcessingException {
        JavaType dtoType = objectMapper.getTypeFactory()
            .constructParametricType(SuccessResponseDto.class, responseDtoClass);

        return objectMapper.readValue(json, dtoType);
    }
}
