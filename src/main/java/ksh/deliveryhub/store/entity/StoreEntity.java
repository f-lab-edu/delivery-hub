package ksh.deliveryhub.store.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class StoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String address;

    private String phone;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private Long ownerId;

    public void update(
        String name,
        String description,
        String address,
        String phone
    ) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
    }

    public void updateStatus(StoreStatus status) {
        this.status = status;
    }

    @Builder
    private StoreEntity(
        Long id,
        String name,
        String description,
        String address,
        String phone,
        FoodCategory foodCategory,
        StoreStatus status,
        Long ownerId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.foodCategory = foodCategory;
        this.status = status;
        this.ownerId = ownerId;
    }
}
