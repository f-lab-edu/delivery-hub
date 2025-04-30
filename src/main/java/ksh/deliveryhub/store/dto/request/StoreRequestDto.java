package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreRequestDto {

    @NotNull(message = "음식 카테고리는 필수입니다.")
    private FoodCategory foodCategory;

    @NotNull(message = "주소는 필수입니다.")
    private String address;

    public Store toModel() {
        return Store.builder()
            .address(getAddress())
            .foodCategory(getFoodCategory())
            .isOpen(false)
            .build();
    }
}
