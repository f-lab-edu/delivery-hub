package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MenuOptionServiceImplTest {

    @Autowired
    MenuOptionServiceImpl menuOptionService;

    @Autowired
    MenuOptionRepository menuOptionRepository;

    @AfterEach
    void tearDown() {
        menuOptionRepository.deleteAllInBatch();
    }

    @Test
    public void 메뉴에_등록된_옵션들의_정보를_수정한다() throws Exception{
        //given
        //메뉴 1의 옵션
        MenuOptionEntity option1 = createMenuOptionEntity("옵션1", 1000, 1L);
        MenuOptionEntity option2 = createMenuOptionEntity("옵션2", 1000, 1L);
        MenuOptionEntity option3 = createMenuOptionEntity("옵션2", 1000, 1L);

        menuOptionRepository.saveAll(List.of(option1, option2, option3));

        //수정 정보
        List<MenuOption> updateInfo = List.of(
            createMenuOption(option1.getId(), "변경 후 옵션1", 1000, 1L),
            createMenuOption(option2.getId(), "옵션2", 1000, 1L),
            createMenuOption(option3.getId(), "변경 후 옵션3", 1500, 1L)

        );

        //when
        List<MenuOption> updateOptions = menuOptionService.updateOptions(updateInfo);

        //then
        assertThat(updateOptions).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(updateInfo);

    }

    @Test
    public void 메뉴에_등록된_옵션들의_정보를_수정할_때_없는_메뉴가_있다면_예외가_발생한다() throws Exception{
        //given
        //메뉴 1의 옵션
        MenuOptionEntity option1 = createMenuOptionEntity("옵션1", 1000, 1L);
        MenuOptionEntity option2 = createMenuOptionEntity("옵션2", 1000, 1L);
        menuOptionRepository.saveAll(List.of(option1, option2));

        //수정 정보
        List<MenuOption> updateInfo = List.of(
            createMenuOption(option1.getId(), "변경 후 옵션1", 1500, 1L),
            createMenuOption(465465L, "변경 후 옵션2", 1500, 1L)
        );

        //when //then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> menuOptionService.updateOptions(updateInfo))
            .returns(ErrorCode.MENU_OPTION_IDS_INVALID, CustomException::getErrorCode);

    }

    private static MenuOption createMenuOption(long id, String name, int price, long menuId) {
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
