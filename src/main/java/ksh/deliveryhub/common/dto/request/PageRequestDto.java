package ksh.deliveryhub.common.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageRequestDto {

    @NotNull(message = "페이지 번호는 필수입니다.")
    @Min(value = 0, message = "페이지 번호는 0 이상입니다.")
    Integer page;

    @NotNull(message = "페이지 크기는 필수입니다.")
    @Max(value = 10L, message = "페이지 크기는 10이하입니다.")
    @Positive(message = "페이지 크기는 양수입니다.")
    Integer size;
}
