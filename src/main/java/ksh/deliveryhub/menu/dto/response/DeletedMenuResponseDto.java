package ksh.deliveryhub.menu.dto.response;

import ksh.deliveryhub.menu.model.MenuOption;
import ksh.deliveryhub.menu.model.MenuWithOptions;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeletedMenuResponseDto {

    private Long menuId;
    private List<Long> optionIds;

    public static DeletedMenuResponseDto from(MenuWithOptions menuWithOptions) {
        Long menuId = menuWithOptions.getMenu().getId();
        List<Long> optionIds = menuWithOptions.getOptions().stream()
            .map(MenuOption::getId)
            .toList();

        return DeletedMenuResponseDto.builder()
            .menuId(menuId)
            .optionIds(optionIds)
            .build();
    }
}
