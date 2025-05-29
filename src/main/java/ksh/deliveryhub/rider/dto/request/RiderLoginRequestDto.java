package ksh.deliveryhub.rider.dto.request;

import jakarta.validation.constraints.NotBlank;
import ksh.deliveryhub.rider.model.Rider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RiderLoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public Rider toModel() {
        return Rider.builder()
            .email(getEmail())
            .password(getPassword())
            .build();
    }
}
