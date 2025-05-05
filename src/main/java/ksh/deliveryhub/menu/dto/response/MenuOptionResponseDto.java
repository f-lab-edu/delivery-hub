package ksh.deliveryhub.menu.dto.response;

import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuOptionResponseDto {

    private Long id;
    private String name;
    private Integer price;

    public static MenuOptionResponseDto from(MenuOption menuOption) {
        return MenuOptionResponseDto.builder()
            .id(menuOption.getId())
            .name(menuOption.getName())
            .price(menuOption.getPrice())
            .build();
    }
}
