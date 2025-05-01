package ksh.deliveryhub.menu.model;

import ksh.deliveryhub.menu.entity.MenuEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Menu {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String image;
    private Long storeId;

    public static Menu from(MenuEntity menuEntity) {
        return Menu.builder()
            .id(menuEntity.getId())
            .name(menuEntity.getName())
            .description(menuEntity.getDescription())
            .price(menuEntity.getPrice())
            .image(menuEntity.getImage())
            .storeId(menuEntity.getStoreId())
            .build();
    }

    public MenuEntity toEntity() {
        return MenuEntity.builder()
            .id(getId())
            .name(getName())
            .description(getDescription())
            .price(getPrice())
            .image(getImage())
            .storeId(getStoreId())
            .build();
    }
}
