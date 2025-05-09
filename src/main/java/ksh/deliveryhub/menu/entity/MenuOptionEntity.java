package ksh.deliveryhub.menu.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu_option")
public class MenuOptionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Long menuId;

    public void updateMenuId(long menuId) {
        this.menuId = menuId;
    }

    public void update(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    @Builder
    private MenuOptionEntity(
        Long id,
        String name,
        Integer price,
        Long menuId
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }
}
