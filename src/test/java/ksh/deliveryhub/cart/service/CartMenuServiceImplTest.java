package ksh.deliveryhub.cart.service;

import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.repository.CartMenuRepository;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class CartMenuServiceImplTest {

    @Autowired
    CartMenuServiceImpl cartMenuService;

    @Autowired
    CartMenuRepository cartMenuRepository;

    @Autowired
    MenuRepository menuRepository;

    @AfterEach
    void tearDown() {
        cartMenuRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_같은_메뉴와_옵션의_품목이_존재하면_수량을_증가시킨다() throws Exception{
        //given
        MenuEntity menuEntity = createMenuEntity(1L);
        menuRepository.save(menuEntity);

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, menuEntity.getId(), 3L, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(menuEntity.getId(), 3L, 2);

        //when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, newCartMenu, 1L);

        //then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(5);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_같은_메뉴와_옵션의_품목이_존재하지_않으면_새로_추가한다() throws Exception{
        //given
        MenuEntity menuEntity = createMenuEntity(1L);
        menuRepository.save(menuEntity);

        CartMenu cartMenu = createCartMenu(menuEntity.getId(), 3L, 2);

        //when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, cartMenu, 1L);

        //then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(2);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_옵션을_선택하지_않은_메뉴도_장바구니에_추가할_수_있다() throws Exception{
        //given
        MenuEntity menuEntity = createMenuEntity(1L);
        menuRepository.save(menuEntity);

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, menuEntity.getId(), null, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(menuEntity.getId(), null, 2);

        //when
        CartMenu addedCartMenu = cartMenuService.addCartMenu(1L, newCartMenu, 1L);

        //then
        assertThat(addedCartMenu.getQuantity()).isEqualTo(5);
    }

    @Test
    public void 장바구니에_메뉴를_추가할_때_다른_가게의_메뉴가_이미_존재하면_메뉴_추가에_실패한다() throws Exception{
        //given
        MenuEntity existingMenuEntity = createMenuEntity(1L);
        MenuEntity newMenuEntity = createMenuEntity(2L);
        menuRepository.saveAll(List.of(existingMenuEntity, newMenuEntity));

        CartMenuEntity existingCartMenuEntity = createCartMenuEntity(1L, existingMenuEntity.getId(), 3L, 3);
        cartMenuRepository.save(existingCartMenuEntity);

        CartMenu newCartMenu = createCartMenu(newMenuEntity.getId(), 43L, 2);

        //when //then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> cartMenuService.addCartMenu(1L, newCartMenu, newMenuEntity.getStoreId()))
            .returns(ErrorCode.CART_MENU_STORE_CONFLICT, CustomException::getErrorCode);
    }

    private static MenuEntity createMenuEntity(long storeId) {
        return MenuEntity.builder()
            .storeId(storeId)
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
