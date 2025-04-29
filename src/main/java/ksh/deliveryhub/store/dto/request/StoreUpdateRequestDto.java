package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreUpdateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    private String description;

    @NotBlank(message = "가게 주소는 필수입니다.")
    private String address;

    @NotBlank(message = "가게 번호는 필수입니다.")
    private String phone;

    public Store toModel(long id) {
        return Store.builder()
            .id(id)
            .name(getName())
            .description(getDescription())
            .address(getAddress())
            .phone(getPhone())
            .build();
    }
}
