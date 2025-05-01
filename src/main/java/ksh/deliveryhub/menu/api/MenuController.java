package ksh.deliveryhub.menu.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.menu.dto.request.MenuCreateRequestDto;
import ksh.deliveryhub.menu.dto.request.MenuOptionCreateRequestDto;
import ksh.deliveryhub.menu.dto.request.MenuUpdateRequestDto;
import ksh.deliveryhub.menu.dto.response.MenuResponseDto;
import ksh.deliveryhub.menu.facade.MenuFacade;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.model.MenuWithOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuFacade menuFacade;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<SuccessResponseDto> registerMenu(
        @PathVariable("storeId") Long storeId,
        @Valid @RequestBody MenuCreateRequestDto request
    ) {
        List<MenuOption> menuOptions = request.getMenuOptions().stream()
            .map(MenuOptionCreateRequestDto::toModel)
            .toList();

        MenuWithOptions menuWithOptions = menuFacade.registerMenu(request.toModel(storeId), menuOptions);

        MenuResponseDto menuResponseDto = MenuResponseDto.from(menuWithOptions);
        SuccessResponseDto<MenuResponseDto> response = SuccessResponseDto.of(menuResponseDto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<SuccessResponseDto> updateMenu(
        @PathVariable("storeId") Long storeId,
        @PathVariable("menuId") Long menuId,
        @Valid @RequestBody MenuUpdateRequestDto request
    ) {
        Menu menu = menuFacade.updateMenu(request.toModel(menuId, storeId));
        MenuResponseDto menuResponseDto = MenuResponseDto.from(menu);
        SuccessResponseDto<MenuResponseDto> response = SuccessResponseDto.of(menuResponseDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<SuccessResponseDto> deleteMenu(
        @PathVariable("storeId") Long storeId,
        @PathVariable("menuId") Long menuId
    ) {
        Menu menu = menuFacade.deleteMenu(menuId, storeId);
        MenuResponseDto menuResponseDto = MenuResponseDto.from(menu);
        SuccessResponseDto<MenuResponseDto> response = SuccessResponseDto.of(menuResponseDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
