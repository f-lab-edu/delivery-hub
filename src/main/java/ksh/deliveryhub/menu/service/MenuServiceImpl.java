package ksh.deliveryhub.menu.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
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

    @Override
    public Menu updateMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.findById(menu.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if(menuEntity.getStoreId() != menu.getStoreId()) {
            throw new CustomException(ErrorCode.MENU_STORE_ID_MISMATCH);
        }

        menuEntity.update(
            menu.getName(),
            menu.getDescription(),
            menu.getMenuStatus(),
            menu.getPrice(),
            menu.getImage()
        );

        return Menu.from(menuEntity);
    }

    @Override
    public Menu deleteMenu(long id, long storeId) {
        MenuEntity menuEntity = menuRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if(menuEntity.getStoreId() != storeId) {
            throw new CustomException(ErrorCode.MENU_STORE_ID_MISMATCH);
        }

        menuRepository.deleteById(id);

        return Menu.from(menuEntity);
    }

    @Override
    public Menu getAvailableMenu(long id) {
        MenuEntity menuEntity = menuRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if(menuEntity.getMenuStatus() != MenuStatus.AVAILABLE){
            throw new CustomException(ErrorCode.MENU_NOT_AVAILABLE);
        }

        return Menu.from(menuEntity);
    }
}
