package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public Menu registerMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.save(menu.toEntity());

        return Menu.from(menuEntity);
    }
}
