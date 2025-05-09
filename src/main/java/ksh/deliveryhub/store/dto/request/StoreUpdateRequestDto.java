package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ksh.deliveryhub.common.util.PhoneNumberUtils;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreUpdateRequestDto {

    @NotNull(message = "가게 id는 필수입니다.")
    private Long id;

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 15, message = "가게 이름은 최대 15자입니다.")
    private String name;

    @NotBlank(message = "가게 설명은 필수입니다.")
    @Size(max = 50, message = "가게 설명은 최대 50자입니다.")
    private String description;

    @NotNull(message = "가게 상태는 필수입니다.")
    private StoreStatus status;

    @NotBlank(message = "가게 주소는 필수입니다.")
    private String address;

    @NotBlank(message = "가게 번호는 필수입니다.")
    private String phone;

    public Store toModel(long id) {
        String trimmedPhoneNumber = PhoneNumberUtils.trim(getPhone());

        return Store.builder()
            .id(id)
            .name(getName())
            .description(getDescription())
            .status(getStatus())
            .address(getAddress())
            .phone(trimmedPhoneNumber)
            .build();
    }
}
