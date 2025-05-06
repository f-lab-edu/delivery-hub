package ksh.deliveryhub.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(404, "store.not.found"),
    STORE_INVALID_PHONE(400, "store.invalid.phone"),
    MENU_NOT_FOUND(404, "menu.not.found"),
    MENU_STORE_ID_MISMATCH(403, "menu.store.id.mismatch"),
    MENU_NOT_AVAILABLE(400, "menu.not.available"),
    MENU_OPTION_NOT_FOUND(404, "menu.option.not.found"),
    MENU_OPTION_IDS_INVALID(400, "menu.option.ids.invalid"),
    CART_MENU_NOT_FOUND(404, "cart.menu.not.found"),
    CART_MENU_STORE_CONFLICT(400, "cart.menu.store.conflict"),
    COUPON_NOT_FOUND(404, "coupon.not.found"),
    COUPON_OUT_OF_STOCK(400, "coupon.out.of.stock");

    private final int status;
    private final String messageKey;
}
