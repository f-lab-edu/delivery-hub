package ksh.deliveryhub.menu.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.menu.dto.request.MenuCreateRequestDto;
import ksh.deliveryhub.menu.dto.response.MenuResponseDto;
import ksh.deliveryhub.menu.facade.MenuFacade;
import ksh.deliveryhub.menu.model.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuFacade menuFacade;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<SuccessResponseDto> registerMenu(
        @PathVariable("storeId") Long storeId,
        @Valid @RequestBody MenuCreateRequestDto request
    ) {
        Menu menu = menuFacade.registerMenu(request.toModel(storeId));
        MenuResponseDto menuResponseDto = MenuResponseDto.from(menu);
        SuccessResponseDto<MenuResponseDto> response = SuccessResponseDto.of(menuResponseDto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}
