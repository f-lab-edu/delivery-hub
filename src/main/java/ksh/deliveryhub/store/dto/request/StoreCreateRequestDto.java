package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ksh.deliveryhub.common.util.PhoneNumberUtils;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreCreateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 15, message = "가게 이름은 최대 15자입니다.")
    private String name;

    @NotBlank(message = "가게 설명은 필수입니다.")
    @Size(max = 50, message = "가게 설명은 최대 50자입니다.")
    private String description;

    @NotBlank(message = "가게 주소는 필수입니다.")
    private String address;

    @NotBlank(message = "가게 번호는 필수입니다.")
    private String phone;

    @NotNull(message = "가게가 제공하는 음식 카테고리는 필수입니다.")
    private FoodCategory foodCategory;

    @NotNull(message = "가게 사장 id는 필수입니다.")
    private Long ownerId;

    public Store toModel() {
        String trimmedPhoneNumber = PhoneNumberUtils.trim(getPhone());

        return Store.builder()
            .name(getName())
            .description(getDescription())
            .address(getAddress())
            .phone(trimmedPhoneNumber)
            .foodCategory(getFoodCategory())
            .ownerId(getOwnerId())
            .status(StoreStatus.CLOSED)
            .build();
    }
}
