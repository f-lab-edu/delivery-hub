package ksh.deliveryhub.store.model;

import ksh.deliveryhub.store.entity.*;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
public class Store {

    private Long id;
    private String name;
    private String description;
    private Address address;
    private Point coordinate;
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
            .coordinate(storeEntity.getCoordinate())
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
            .coordinate(getCoordinate())
            .phone(getPhone())
            .foodCategory(getFoodCategory())
            .status(getStatus())
            .ownerId(getOwnerId())
            .build();
    }
}
