package ksh.deliveryhub.common.dto.response;

import lombok.Getter;

@Getter
public class SuccessResponseDto<T> {
    private final T data;

    private SuccessResponseDto(T data) {
        this.data = data;
    }

    public static <T> SuccessResponseDto<T> of(T data) {
        return new SuccessResponseDto<>(data);
    }
}
