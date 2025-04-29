package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.store.entity.FoodCategory;
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

    @NotNull(message = "가게가 제공하는 음식 카테고리는 필수입니다.")
    private FoodCategory foodCategory;

    public Store toModel(long id) {
        return Store.builder()
            .id(id)
            .name(getName())
            .description(getDescription())
            .address(getAddress())
            .phone(getPhone())
            .foodCategory(getFoodCategory())
            .build();
    }
}
