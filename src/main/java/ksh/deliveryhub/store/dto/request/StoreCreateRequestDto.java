package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreCreateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "가게 설명은 필수입니다.")
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
        return Store.builder()
            .name(getName())
            .description(getDescription())
            .address(getAddress())
            .phone(getPhone())
            .foodCategory(getFoodCategory())
            .ownerId(getOwnerId())
            .isOpen(false)
            .build();
    }
}
