package ksh.deliveryhub.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponseDto of(int status, String code, String message) {
        return new ErrorResponseDto(status, code, message);
    }

    public static ErrorResponseDto error() {
        return new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name(),
            "Internal Server Error"
        );
    }
}
