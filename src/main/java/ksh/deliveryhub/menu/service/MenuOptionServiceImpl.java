package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuOptionServiceImpl implements MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    @Transactional
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

    @Transactional
    @Override
    public List<MenuOption> updateOptions(List<MenuOption> menuOptions) {
        Map<Long, MenuOption> menuOptionMap = menuOptions.stream()
            .collect(Collectors.toMap(MenuOption::getId, Function.identity()));

        List<MenuOptionEntity> menuOptionEntities = menuOptionRepository.findAllById(menuOptionMap.keySet());
        if (menuOptionEntities.size() != menuOptionMap.size()) {
            throw new CustomException(ErrorCode.MENU_OPTION_IDS_INVALID);
        }
        for (MenuOptionEntity menuOptionEntity : menuOptionEntities) {
            MenuOption menuOption = menuOptionMap.get(menuOptionEntity.getId());
            menuOptionEntity.update(menuOption.getName(), menuOption.getPrice());
        }

        return menuOptionEntities.stream()
            .map(MenuOption::from)
            .toList();
    }
}
