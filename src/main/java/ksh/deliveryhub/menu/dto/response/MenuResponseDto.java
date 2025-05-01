package ksh.deliveryhub.menu.dto.response;

import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.model.Menu;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuResponseDto {

    private Long id;
    private String name;
    private String description;
    private MenuStatus menuStatus;
    private Integer price;
    private String image;
    private Long storeId;
    private LocalDateTime createdAt;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .name(menu.getName())
            .description(menu.getDescription())
            .menuStatus(menu.getMenuStatus())
            .price(menu.getPrice())
            .image(menu.getImage())
            .storeId(menu.getStoreId())
            .createdAt(menu.getCreatedAt())
            .build();
    }
}
