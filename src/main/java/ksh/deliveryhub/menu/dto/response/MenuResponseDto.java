package ksh.deliveryhub.menu.dto.response;

import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuWithOptions;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MenuResponseDto {

    private Long id;
    private String name;
    private String description;
    private MenuStatus status;
    private Integer price;
    private String image;
    private Long storeId;
    private LocalDateTime createdAt;
    private List<MenuOptionResponseDto> options = new ArrayList<>();

    public static MenuResponseDto from(MenuWithOptions menuWithOptions) {
        Menu menu = menuWithOptions.getMenu();
        List<MenuOptionResponseDto> menuOptionResponseDtos = menuWithOptions.getOptions().stream()
            .map(MenuOptionResponseDto::from)
            .toList();

        return MenuResponseDto.builder()
            .id(menu.getId())
            .name(menu.getName())
            .description(menu.getDescription())
            .status(menu.getMenuStatus())
            .price(menu.getPrice())
            .image(menu.getImage())
            .storeId(menu.getStoreId())
            .createdAt(menu.getCreatedAt())
            .options(menuOptionResponseDtos)
            .build();
    }

    //컴파일 통과를 위한 오버로드 메소드
    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .name(menu.getName())
            .description(menu.getDescription())
            .status(menu.getMenuStatus())
            .price(menu.getPrice())
            .image(menu.getImage())
            .storeId(menu.getStoreId())
            .createdAt(menu.getCreatedAt())
            .build();
    }
}
