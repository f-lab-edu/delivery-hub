package ksh.deliveryhub.store.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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

    @Embedded
    private Address address;

    @Column(columnDefinition = "point not null srid 4326")
    private Point coordinate;

    private String phone;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    private Long ownerId;

    public void update(
        String name,
        String description,
        Address address,
        StoreStatus status,
        String phone
    ) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.status = status;
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
        Address address,
        Point coordinate,
        String phone,
        FoodCategory foodCategory,
        StoreStatus status,
        Long ownerId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.coordinate = coordinate;
        this.phone = phone;
        this.foodCategory = foodCategory;
        this.status = status;
        this.ownerId = ownerId;
    }
}
