package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuOptionServiceImpl implements MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    @Override
    public List<MenuOption> registerMenuOptions(long menuId, List<MenuOption> menuOptions) {
        List<MenuOptionEntity> menuOptionEntities = menuOptions.stream()
            .map(menuOption -> {
                MenuOptionEntity menuOptionEntity = menuOption.toEntity();
                menuOptionEntity.updateMenuId(menuId);
                return menuOptionEntity;
            })
            .toList();

        return menuOptionRepository.saveAll(menuOptionEntities).stream()
            .map(MenuOption::from)
            .toList();
    }

    @Override
    public List<MenuOption> updateOptions(List<MenuOption> menuOptions) {
        List<Long> ids = menuOptions.stream()
            .map(MenuOption::getId)
            .toList();

        List<MenuOptionEntity> entities = menuOptionRepository.findAllById(ids);
        if (entities.size() != ids.size()) {
            throw new CustomException(ErrorCode.MENU_OPTION_IDS_INVALID);
        }

        Map<Long, MenuOptionEntity> entityMap = entities.stream()
            .collect(Collectors.toMap(
                MenuOptionEntity::getId,
                Function.identity()
            ));

        for (MenuOption option : menuOptions) {
            MenuOptionEntity entity = entityMap.get(option.getId());
            entity.update(option.getName(), option.getPrice());
        }

        return menuOptions.stream()
            .map(option -> MenuOption.from(entityMap.get(option.getId())))
            .toList();
    }


    @Override
    public List<MenuOption> deleteMenuOptionsOfMenu(long menuId) {
        List<MenuOptionEntity> menuOptionEntities = menuOptionRepository.findByMenuId(menuId);
        menuOptionRepository.deleteAll(menuOptionEntities);

        return menuOptionEntities.stream()
            .map(MenuOption::from)
            .toList();
    }

    @Override
    public MenuOption getOptionIsInMenu(long id, long menuId) {
        MenuOptionEntity optionEntity = menuOptionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        if(optionEntity.getMenuId() != menuId) {
            throw new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND);
        }

        return MenuOption.from(optionEntity);
    }

    @Override
    public List<MenuOption> findOptionsOfCartMenu(List<CartMenu> cartMenus) {
        List<Long> ids = cartMenus.stream()
            .map(CartMenu::getOptionId)
            .toList();

        return menuOptionRepository.findAllById(ids).stream()
            .map(MenuOption::from)
            .toList();
    }
}
