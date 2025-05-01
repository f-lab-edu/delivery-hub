package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
