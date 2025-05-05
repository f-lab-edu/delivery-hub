package ksh.deliveryhub.menu.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
public class MenuEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    private Integer price;

    private String image;

    private Long storeId;

    public void update(
        String name,
        String description,
        MenuStatus menuStatus,
        Integer price,
        String image
    ) {
        this.name = name;
        this.description = description;
        this.menuStatus = menuStatus;
        this.price = price;
        this.image = image;
    }

    @Builder
    private MenuEntity(
        Long id,
        String name,
        String description,
        MenuStatus menuStatus,
        Integer price,
        String image,
        Long storeId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menuStatus = menuStatus;
        this.price = price;
        this.image = image;
        this.storeId = storeId;
    }
}
