package ksh.deliveryhub.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(404, "store.not.found");

    private final int status;
    private final String messageKey;
}
