package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    @Override
    public Menu registerMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.save(menu.toEntity());

        return Menu.from(menuEntity);
    }

    @Transactional
    @Override
    public Menu updateMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.findById(menu.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        menuEntity.update(
            menu.getName(),
            menu.getDescription(),
            menu.getMenuStatus(),
            menu.getPrice(),
            menu.getImage()
        );

        return Menu.from(menuEntity);
    }
}
