package ksh.deliveryhub.cart.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.cart.dto.response.CartMenuResponseDto;
import ksh.deliveryhub.cart.dto.response.CartResponseDto;
import ksh.deliveryhub.cart.entity.CartEntity;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.cart.repository.CartRepository;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import ksh.deliveryhub.menu.repository.MenuRepository;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static ksh.deliveryhub.cart.entity.CartStatus.ACTIVE;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartMenuRepository cartMenuRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuOptionRepository menuOptionRepository;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        cartMenuRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuOptionRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @Test
    public void 장바구니에_담긴_메뉴_정보를_조회에_성공하면_201응답을_받는다() throws Exception {
        //given
        //가게 생성
        StoreEntity storeEntity = StoreEntity.builder().build();
        storeRepository.save(storeEntity);

        //카트 생성
        CartEntity cartEntity = createCartEntity(1L);
        cartRepository.save(cartEntity);

        //메뉴 생성
        MenuEntity menuEntity1 = createMenuEntity("치즈 피자", "치즈 들어감", 10000, storeEntity.getId());
        MenuEntity menuEntity2 = createMenuEntity("페퍼로니 피자", "페퍼로니 들어감", 15000, storeEntity.getId());
        menuRepository.saveAll(List.of(menuEntity1, menuEntity2));

        //메뉴의 옵션 생성
        MenuOptionEntity menuOptionEntity = createMenuOptionEntity("치즈 추가", 500, menuEntity1.getId());
        menuOptionRepository.save(menuOptionEntity);

        //장바구니에 담긴 메뉴 생성
        CartMenuEntity cartMenuEntity1 = createCartMenuEntity(cartEntity.getId(), menuEntity1.getId(), menuOptionEntity.getId(), 2);
        CartMenuEntity cartMenuEntity2 = createCartMenuEntity(cartEntity.getId(), menuEntity2.getId(), null, 1);
        cartMenuRepository.saveAll(List.of(cartMenuEntity1, cartMenuEntity2));

        //when
        MvcResult mvcResult = mockMvc.perform(
                get("/users/{userId}/carts/menus", 1L)
            ).andDo(print())
            .andReturn();

        //then
        String json = mvcResult.getResponse().getContentAsString();
        SuccessResponseDto<CartResponseDto> response =
            objectMapper.readValue(
                json,
                new TypeReference<SuccessResponseDto<CartResponseDto>>() {}
            );

        CartResponseDto dto = response.getData();
        List<CartMenuResponseDto> menus = dto.getMenus();

        assertThat(menus).hasSize(2);

        CartMenuResponseDto first = menus.get(0);
        assertThat(first.getQuantity()).isEqualTo(2);
        assertThat(first.getMenuName()).isEqualTo(menuEntity1.getName());
        assertThat(first.getMenuPrice()).isEqualTo(menuEntity1.getPrice());
        assertThat(first.getOptionName()).isEqualTo(menuOptionEntity.getName());
        assertThat(first.getOptionPrice()).isEqualTo(menuOptionEntity.getPrice());

        CartMenuResponseDto second = menus.get(1);
        assertThat(second.getQuantity()).isEqualTo(1);
        assertThat(second.getMenuName()).isEqualTo(menuEntity2.getName());
        assertThat(second.getMenuPrice()).isEqualTo(menuEntity2.getPrice());
        assertThat(second.getOptionName()).isNull();
        assertThat(second.getOptionPrice()).isNull();
    }

    private static CartEntity createCartEntity(long userId) {
        return CartEntity.builder()
            .userId(userId)
            .status(ACTIVE)
            .build();
    }

    private static MenuEntity createMenuEntity(String name, String description, int price, long storeId) {
        return MenuEntity.builder()
            .name(name)
            .description(description)
            .price(price)
            .storeId(storeId)
            .image("https://example.com/image.png")
            .build();
    }

    private static MenuOptionEntity createMenuOptionEntity(String name, int price, long menuId) {
        return MenuOptionEntity.builder()
            .name(name)
            .price(price)
            .menuId(menuId)
            .build();
    }

    private static CartMenuEntity createCartMenuEntity(long cartId, long menuId, Long optionId, int quantity) {
        return CartMenuEntity.builder()
            .cartId(cartId)
            .menuId(menuId)
            .optionId(optionId)
            .quantity(quantity)
            .build();
    }
}
