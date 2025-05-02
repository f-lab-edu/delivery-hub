package ksh.deliveryhub.store.model;

import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.entity.StoreStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Store {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private FoodCategory foodCategory;
    private StoreStatus status;
    private Long ownerId;

    public static Store from(StoreEntity storeEntity) {
        return Store.builder()
            .id(storeEntity.getId())
            .name(storeEntity.getName())
            .description(storeEntity.getDescription())
            .address(storeEntity.getAddress())
            .phone(storeEntity.getPhone())
            .foodCategory(storeEntity.getFoodCategory())
            .status(storeEntity.getStatus())
            .ownerId(storeEntity.getOwnerId())
            .build();
    }

    public StoreEntity toEntity() {
        return StoreEntity.builder()
            .id(getId())
            .name(getName())
            .description(getDescription())
            .address(getAddress())
            .phone(getPhone())
            .foodCategory(getFoodCategory())
            .status(getStatus())
            .build();
    }
}
