package ksh.deliveryhub.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuOptionCreateRequestDto {

    @NotBlank(message = "메뉴 옵션 이름은 필수입니다.")
    @Size(max = 20, message = "메뉴 옵션 이름의 길이는 최대 20자입니다.")
    private String name;

    @NotNull(message = "메뉴 옵션 가격은 필수입니다.")
    @Positive(message = "메뉴 옵션의 가격은 양수입니다.")
    private Integer price;

    public MenuOption toModel() {
        return MenuOption.builder()
            .name(getName())
            .price(getPrice())
            .build();
    }
}
