package ksh.deliveryhub.store.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.store.dto.request.StoreRequestDto;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/stores")
    public ResponseEntity<SuccessResponseDto> findOpenStores(
        @Valid StoreRequestDto storeRequestDto,
        @Valid PageRequestDto pageRequestDto
    ) {
        PageResult<Store> storePage = storeService.findOpenStores(storeRequestDto, pageRequestDto);
        SuccessResponseDto<PageResult<Store>> response = SuccessResponseDto.of(storePage);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
