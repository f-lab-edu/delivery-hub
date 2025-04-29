package ksh.deliveryhub.store.dto.response;

import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponseDto {

    private String name;
    private String description;
    private String address;
    private String phone;
    private FoodCategory foodCategory;
    private boolean isOpen;

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
            .name(store.getName())
            .description(store.getDescription())
            .address(store.getAddress())
            .phone(store.getPhone())
            .foodCategory(store.getFoodCategory())
            .isOpen(store.isOpen())
            .build();
    }
}
