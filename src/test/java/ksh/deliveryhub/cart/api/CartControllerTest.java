package ksh.deliveryhub.cart.api;

import ksh.deliveryhub.cart.entity.CartEntity;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.cart.repository.CartRepository;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import ksh.deliveryhub.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static ksh.deliveryhub.cart.entity.CartStatus.ACTIVE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartMenuRepository cartMenuRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuOptionRepository menuOptionRepository;

    @Test
    public void 장바구니에_메뉴를_추가하고_201응답을_받는다() throws Exception {
        //given
        //카트 생성
        CartEntity cartEntity = createCartEntity(1L);
        cartRepository.save(cartEntity);

        //메뉴 생성
        MenuEntity menuEntity1 = createMenuEntity("치즈 피자", "치즈 들어감", 10000, 1L);
        MenuEntity menuEntity2 = createMenuEntity("페퍼로니 피자", "페퍼로니 들어감", 15000, 1L);
        menuRepository.saveAll(List.of(menuEntity1, menuEntity2));

        //메뉴의 옵션 생성
        MenuOptionEntity menuOptionEntity = createMenuOptionEntity("치즈 추가", 500, menuEntity1.getId());
        menuOptionRepository.save(menuOptionEntity);

        //장바구니에 담긴 메뉴 생성
        CartMenuEntity cartMenuEntity1 = createCartMenuEntity(cartEntity.getId(), menuEntity1.getId(), menuOptionEntity.getId(), 2);
        CartMenuEntity cartMenuEntity2 = createCartMenuEntity(cartEntity.getId(), menuEntity2.getId(), null, 1);
        cartMenuRepository.saveAll(List.of(cartMenuEntity1, cartMenuEntity2));

        //when //then
        mockMvc.perform(
                get("/carts")
                    .param("userId", "1")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.menus").isArray())
            .andExpect(jsonPath("$.data.menus[0].quantity").value(2))
            .andExpect(jsonPath("$.data.menus[0].menuName").value(menuEntity1.getName()))
            .andExpect(jsonPath("$.data.menus[0].menuPrice").value(menuEntity1.getPrice()))
            .andExpect(jsonPath("$.data.menus[0].optionName").value(menuOptionEntity.getName()))
            .andExpect(jsonPath("$.data.menus[0].optionPrice").value(menuOptionEntity.getPrice()))
            .andExpect(jsonPath("$.data.menus[1].quantity").value(1))
            .andExpect(jsonPath("$.data.menus[1].menuName").value(menuEntity2.getName()))
            .andExpect(jsonPath("$.data.menus[1].menuPrice").value(menuEntity2.getPrice()))
            .andExpect(jsonPath("$.data.menus[1].optionName").isEmpty())
            .andExpect(jsonPath("$.data.menus[1].optionPrice").isEmpty());
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
