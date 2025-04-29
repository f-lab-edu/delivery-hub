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

    private boolean isOpen;

    private Long ownerId;

    @Builder
    private StoreEntity(
        Long id,
        String name,
        String description,
        String address,
        String phone,
        FoodCategory foodCategory,
        boolean isOpen,
        Long ownerId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.foodCategory = foodCategory;
        this.isOpen = isOpen;
        this.ownerId = ownerId;
    }
}
