package ksh.deliveryhub.store.dto.response;

import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponseDto {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private FoodCategory foodCategory;
    private StoreStatus status;

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
            .id(store.getId())
            .name(store.getName())
            .description(store.getDescription())
            .address(store.getAddress())
            .phone(store.getPhone())
            .foodCategory(store.getFoodCategory())
            .status(store.getStatus())
            .build();
    }
}
