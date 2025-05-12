package ksh.deliveryhub.menu.facade;

import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.model.MenuWithOptions;
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

@SpringBootTest
class MenuFacadeTest {

    @Autowired
    MenuFacade menuFacade;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuOptionRepository menuOptionRepository;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        menuRepository.deleteAllInBatch();
        menuOptionRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @Test
    public void 가게에_새로운_메뉴를_등록한다() throws Exception {
        //given
        //가게
        StoreEntity storeEntity = StoreEntity.builder().build();
        storeRepository.save(storeEntity);

        //메뉴
        Menu menu = createMenu(null, "피자", MenuStatus.AVAILABLE, 10000, storeEntity.getId());

        //옵션
        List<MenuOption> options = List.of(
            createMenuOption(null, "옵션1", 1000, null),
            createMenuOption(null, "옵션2", 1000, null)
        );

        //when
        MenuWithOptions menuWithOptions = menuFacade.registerMenu(menu, options);

        //then
        assertThat(menuWithOptions.getMenu().getId()).isNotNull();

        assertThat(menuWithOptions.getMenu()).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(menu);

        assertThat(menuWithOptions.getOptions()).hasSize(2)
            .extracting("id")
            .isNotNull();

        assertThat(menuWithOptions.getOptions())
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "menuId")
            .containsExactlyElementsOf(options);
    }

    @Test
    public void 메뉴와_옵션_정보를_수정한다() throws Exception {
        //given
        //가게
        StoreEntity storeEntity = StoreEntity.builder().build();
        storeRepository.save(storeEntity);

        //기존 메뉴
        MenuEntity menuEntity = createMenuEntity("피자", MenuStatus.AVAILABLE, 10000, storeEntity.getId());
        menuRepository.save(menuEntity);

        //기존 메뉴의 옵션
        MenuOptionEntity option1 = createMenuOptionEntity("옵션1", 1000, menuEntity.getId());
        MenuOptionEntity option2 = createMenuOptionEntity("옵션2", 1000, menuEntity.getId());
        menuOptionRepository.saveAll(List.of(option1, option2));

        //수정 요청 정보
        Menu updateMenuInfo = createMenu(menuEntity.getId(), "피자", MenuStatus.UNAVAILABLE, 15000, storeEntity.getId());

        List<MenuOption> updateOptionsInfo = List.of(
            createMenuOption(option1.getId(), "변경 후 옵션1", 1000, menuEntity.getId()),
            createMenuOption(option2.getId(), "옵션2", 1000, menuEntity.getId())
        );

        //when
        MenuWithOptions menuWithOptions = menuFacade.registerMenu(updateMenuInfo, updateOptionsInfo);

        //then
        assertThat(menuWithOptions.getMenu()).usingRecursiveComparison()
            .isEqualTo(updateMenuInfo);

        assertThat(menuWithOptions.getOptions())
            .usingRecursiveComparison()
            .isEqualTo(updateOptionsInfo);
    }

    private static Menu createMenu(Long id, String name, MenuStatus status, int price, long storeId) {
        return Menu.builder()
            .id(id)
            .name(name)
            .menuStatus(status)
            .price(price)
            .storeId(storeId)
            .build();
    }

    private static MenuEntity createMenuEntity(String name, MenuStatus status, int price, long storeId) {
        return MenuEntity.builder()
            .name(name)
            .menuStatus(status)
            .price(price)
            .storeId(storeId)
            .build();
    }

    private static MenuOption createMenuOption(Long id, String name, int price, Long menuId) {
        return MenuOption.builder()
            .id(id)
            .name(name)
            .price(price)
            .menuId(menuId)
            .build();
    }

    private static MenuOptionEntity createMenuOptionEntity(String name, int price, long menuId) {
        return MenuOptionEntity.builder()
            .name(name)
            .price(price)
            .menuId(menuId)
            .build();
    }
}
