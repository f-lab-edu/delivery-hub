package ksh.deliveryhub.menu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer price;

    private String image;

    private Long storeId;

    @Builder
    private Menu(
        String name,
        String description,
        Integer price,
        String image,
        Long storeId
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.storeId = storeId;
    }
}
