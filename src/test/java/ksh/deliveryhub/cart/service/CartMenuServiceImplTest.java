package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import ksh.deliveryhub.menu.repository.MenuRepository;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest
class CartMenuServiceImplTest {

    @Autowired
    private CartMenuServiceImpl cartMenuService;

    @Autowired
    private CartMenuRepository cartMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuOptionRepository menuOptionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        cartMenuRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuOptionRepository.deleteAllInBatch();
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_같은_메뉴와_옵션의_품목이_존재하면_수량을_증가시킨다() {
        // given
        MenuEntity menuEntity = createMenuEntity("테스트 메뉴", 1000, 1L);
        menuRepository.save(menuEntity);

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, menuEntity.getId(), 3L, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(menuEntity.getId(), 3L, 2);

        // when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, newCartMenu, 1L);

        // then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(5);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_같은_메뉴와_옵션의_품목이_존재하지_않으면_새로_추가한다() {
        // given
        MenuEntity menuEntity = createMenuEntity("테스트 메뉴", 1000, 1L);
        menuRepository.save(menuEntity);

        CartMenu cartMenu = createCartMenu(menuEntity.getId(), 3L, 2);

        // when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, cartMenu, 1L);

        // then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(2);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_옵션을_선택하지_않은_메뉴도_장바구니에_추가할_수_있다() {
        // given
        MenuEntity menuEntity = createMenuEntity("테스트 메뉴", 1000, 1L);
        menuRepository.save(menuEntity);

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, menuEntity.getId(), null, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(menuEntity.getId(), null, 2);

        // when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, newCartMenu, 1L);

        // then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(5);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_다른_가게의_메뉴가_이미_존재하면_메뉴_추가에_실패한다() {
        // given
        MenuEntity existingMenuEntity = createMenuEntity("메뉴A", 1000, 1L);
        MenuEntity newMenuEntity      = createMenuEntity("메뉴B", 1000, 2L);
        menuRepository.saveAll(List.of(existingMenuEntity, newMenuEntity));

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, existingMenuEntity.getId(), 3L, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(newMenuEntity.getId(), 43L, 2);

        // when / then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> cartMenuService.addCartMenu(1L, newCartMenu, newMenuEntity.getStoreId()))
            .returns(ErrorCode.CART_MENU_STORE_CONFLICT, CustomException::getErrorCode);
    }

    @Test
    public void 장바구니에_담긴_메뉴의_수량을_변경한다() {
        // given
        CartMenuEntity cartMenuEntity = createCartMenuEntity(1L, 1L, 3L, 3);
        cartMenuRepository.save(cartMenuEntity);

        CartMenu cartMenu = CartMenu.builder()
            .id(cartMenuEntity.getId())
            .quantity(1)
            .build();

        // when
        cartMenuService.changeQuantity(1L, cartMenu);

        // then
        CartMenuEntity updated = cartMenuRepository.findById(cartMenuEntity.getId()).get();
        assertThat(updated.getQuantity()).isEqualTo(1);
    }

    @Test
    public void 장바구니에_담긴_메뉴의_수량을_변경할_때_수량이_0이면_메뉴를_장바구니에서_삭제한다() {
        // given
        CartMenuEntity cartMenuEntity = createCartMenuEntity(1L, 1L, 3L, 3);
        cartMenuRepository.save(cartMenuEntity);

        CartMenu cartMenu = CartMenu.builder()
            .id(cartMenuEntity.getId())
            .quantity(0)
            .build();

        // when
        cartMenuService.changeQuantity(1L, cartMenu);

        // then
        assertThat(cartMenuRepository.existsById(cartMenuEntity.getId())).isFalse();
    }

    @Test
    public void 장바구니에_담긴_메뉴의_수량을_변경할_때_사용자의_장바구니에_없는_메뉴는_수량을_변경할_수_없다() {
        // given
        CartMenuEntity cartMenuEntity = createCartMenuEntity(1L, 1L, 3L, 3);
        cartMenuRepository.save(cartMenuEntity);

        CartMenu cartMenu = CartMenu.builder()
            .id(3L)
            .quantity(1)
            .build();

        // when / then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> cartMenuService.changeQuantity(1L, cartMenu))
            .returns(ErrorCode.CART_MENU_NOT_FOUND, CustomException::getErrorCode);
    }

    @Test
    public void 장바구니에_담긴_메뉴를_메뉴_옵션_정보와_함께_조회한다() {
        // given
        StoreEntity storeEntity = StoreEntity.builder().build();
        storeRepository.save(storeEntity);

        MenuEntity menuEntity1 = createMenuEntity("일반 피자", 10000, storeEntity.getId());
        MenuEntity menuEntity2 = createMenuEntity("고급 피자", 15000, storeEntity.getId());
        menuRepository.saveAll(List.of(menuEntity1, menuEntity2));

        MenuOptionEntity optionEntity = createMenuOptionEntity("치즈 추가", 500, menuEntity1.getId());
        menuOptionRepository.save(optionEntity);

        CartMenuEntity cartMenuEntity1 = createCartMenuEntity(1L, menuEntity1.getId(), optionEntity.getId(), 2);
        CartMenuEntity cartMenuEntity2 = createCartMenuEntity(1L, menuEntity2.getId(), null, 1);
        cartMenuRepository.saveAll(List.of(cartMenuEntity1, cartMenuEntity2));

        // when
        List<CartMenuDetail> details = cartMenuService.findCartMenusWithDetail(1L);

        // then
        assertThat(details).hasSize(2)
            .extracting("quantity", "menuId", "menuPrice", "optionId", "optionPrice")
            .containsExactlyInAnyOrder(
                tuple(2, menuEntity1.getId(), menuEntity1.getPrice(), optionEntity.getId(), optionEntity.getPrice()),
                tuple(1, menuEntity2.getId(), menuEntity2.getPrice(), null, null)
            );
    }

    private static MenuEntity createMenuEntity(String name, int price, long storeId) {
        return MenuEntity.builder()
            .name(name)
            .description("설명")
            .menuStatus(MenuStatus.AVAILABLE)
            .price(price)
            .image("https://example.com/image.png")
            .storeId(storeId)
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

    private static CartMenu createCartMenu(long menuId, Long optionId, int quantity) {
        return CartMenu.builder()
            .menuId(menuId)
            .optionId(optionId)
            .quantity(quantity)
            .build();
    }
}
