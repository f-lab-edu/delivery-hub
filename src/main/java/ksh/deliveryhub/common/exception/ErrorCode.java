package ksh.deliveryhub.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {

    STORE_NOT_FOUND(404, "store.not.found");

    private final int status;
    private final String messageKey;
}
