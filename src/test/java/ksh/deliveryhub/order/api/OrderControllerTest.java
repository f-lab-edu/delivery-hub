package ksh.deliveryhub.order.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.dto.response.PageResult;
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
import ksh.deliveryhub.order.dto.response.AcceptedOrderResponseDto;
import ksh.deliveryhub.order.dto.response.OrderCreateResponseDto;
import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderItemEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.repository.OrderItemRepository;
import ksh.deliveryhub.order.repository.OrderRepository;
import ksh.deliveryhub.point.entity.UserPointEntity;
import ksh.deliveryhub.point.repository.UserPointRepository;
import ksh.deliveryhub.store.entity.Address;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @AfterEach
    void tearDown() {
        cartMenuRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
        userPointRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuOptionRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        orderItemRepository.deleteAllInBatch();
    }

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

    @Test
    public void 배차_대기_중인_주문을_조회하고_200_응답을_받는다() throws Exception {
        //given
        StoreEntity storeEntity = createStore();
        storeRepository.save(storeEntity);

        MenuEntity menuEntity1 = createMenu("피자", 10000, storeEntity.getId());
        MenuEntity menuEntity2 = createMenu("치즈 피자", 15000, storeEntity.getId());
        menuRepository.saveAll(List.of(menuEntity1, menuEntity2));

        OrderEntity orderEntity1 = createOrderEntity(OrderStatus.ACCEPTED, storeEntity.getId());
        OrderEntity orderEntity2 = createOrderEntity(OrderStatus.ACCEPTED, storeEntity.getId());
        orderRepository.saveAll(List.of(orderEntity1, orderEntity2));

        OrderItemEntity order1ItemEntity1 = createOrderItemEntity(orderEntity1.getId(), menuEntity1.getId(), 2);
        OrderItemEntity order1ItemEntity2 = createOrderItemEntity(orderEntity1.getId(), menuEntity2.getId(), 5);
        OrderItemEntity order2ItemEntity1 = createOrderItemEntity(orderEntity2.getId(), menuEntity1.getId(), 42);
        orderItemRepository.saveAll(List.of(order1ItemEntity1, order1ItemEntity2, order2ItemEntity1));

        //when
        String json = mockMvc.perform(
                get("/orders/paid")
                    .param("city", "서울시")
                    .param("district", "강서구")
                    .param("lastCreatedAt", order1ItemEntity1.getCreatedAt().minusDays(1).toString())
                    .param("page", "0")
                    .param("size", "3")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        //then
        JsonNode dataNode = objectMapper.readTree(json).get("data");

        PageResult<AcceptedOrderResponseDto> pageResult =
            objectMapper.readValue(
                dataNode.traverse(),
                new TypeReference<>() {
                }
            );

        List<AcceptedOrderResponseDto> orders = pageResult.getContent();
        assertThat(orders).hasSize(2)
            .extracting("id", "storeName", "address")
            .containsExactlyInAnyOrder(
                tuple(orderEntity1.getId(), storeEntity.getName(), storeEntity.getAddress().toString()),
                tuple(orderEntity2.getId(), storeEntity.getName(), storeEntity.getAddress().toString())
            );
        assertThat(orders.get(0).getMenus()).hasSize(2)
            .extracting("name", "quantity")
            .containsExactlyInAnyOrder(
                tuple(menuEntity1.getName(), order1ItemEntity1.getQuantity()),
                tuple(menuEntity2.getName(), order1ItemEntity2.getQuantity())
            );
        assertThat(orders.get(1).getMenus()).hasSize(1)
            .extracting("name", "quantity")
            .containsExactlyInAnyOrder(
                tuple(menuEntity1.getName(), order2ItemEntity1.getQuantity())
            );
    }

    private static StoreEntity createStore() {
        Address address = Address.of("서울시", "강서구", "방화동", "1로", "1동");

        return StoreEntity.builder()
            .foodCategory(FoodCategory.PIZZA)
            .address(address)
            .phone("010-1234-5678")
            .name("음식점")
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

    private OrderEntity createOrderEntity(OrderStatus orderStatus, Long storeId) {
        return OrderEntity.builder()
            .orderStatus(orderStatus)
            .storeId(storeId)
            .build();
    }

    private OrderItemEntity createOrderItemEntity(long orderId, long menuId, int quantity) {
        return OrderItemEntity.builder()
            .orderId(orderId)
            .menuId(menuId)
            .quantity(quantity)
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
