package ksh.deliveryhub.menu.model;

import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuOption {

    private Long id;
    private String name;
    private Integer price;
    private Long menuId;

    public static MenuOption from(MenuOptionEntity menuOptionEntity) {
        return MenuOption.builder()
            .id(menuOptionEntity.getId())
            .name(menuOptionEntity.getName())
            .price(menuOptionEntity.getPrice())
            .menuId(menuOptionEntity.getMenuId())
            .build();
    }

    public MenuOptionEntity toEntity() {
        return MenuOptionEntity.builder()
            .id(getId())
            .name(getName())
            .price(getPrice())
            .menuId(getMenuId())
            .build();
    }
}
