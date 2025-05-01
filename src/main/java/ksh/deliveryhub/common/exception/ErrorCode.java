package ksh.deliveryhub.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(404, "store.not.found"),
    STORE_INVALID_PHONE(400, "store.invalid.phone"),
    MENU_NOT_FOUND(404, "menu.not.found"),
    MENU_STORE_ID_MISMATCH(403, "menu.store.id.mismatch");

    private final int status;
    private final String messageKey;
}
