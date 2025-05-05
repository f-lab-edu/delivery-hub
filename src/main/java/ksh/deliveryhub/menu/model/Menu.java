package ksh.deliveryhub.menu.model;

import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Menu {

    private Long id;
    private String name;
    private String description;
    private MenuStatus menuStatus;
    private Integer price;
    private String image;
    private Long storeId;

    public static Menu from(MenuEntity menuEntity) {
        return Menu.builder()
            .id(menuEntity.getId())
            .name(menuEntity.getName())
            .description(menuEntity.getDescription())
            .menuStatus(menuEntity.getMenuStatus())
            .price(menuEntity.getPrice())
            .image(menuEntity.getImage())
            .menuStatus(menuEntity.getMenuStatus())
            .storeId(menuEntity.getStoreId())
            .build();
    }

    public MenuEntity toEntity() {
        return MenuEntity.builder()
            .id(getId())
            .name(getName())
            .description(getDescription())
            .menuStatus(getMenuStatus())
            .price(getPrice())
            .image(getImage())
            .storeId(getStoreId())
            .build();
    }
}
