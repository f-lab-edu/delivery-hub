package ksh.deliveryhub.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ksh.deliveryhub.menu.model.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuUpdateRequestDto {


    @NotBlank(message = "메뉴명은 필수입니다.")
    @Size(max = 15, message = "메뉴명은 최대 15자입니다.")
    private String name;

    @NotBlank(message = "메뉴 설명은 필수입니다.")
    @Size(max = 50, message = "메뉴 설명은 최대 50자입니다.")
    private String description;

    @NotNull(message = "메뉴 가격은 필수입니다.")
    @Positive(message = "메뉴 가격은 양수입니다.")
    private Integer price;

    @NotBlank(message = "메뉴 예시 이미지 url은 필수입니다.")
    private String image;

    public Menu toModel(long id, long storeId) {
        return Menu.builder()
            .id(id)
            .name(getName())
            .description(getDescription())
            .price(getPrice())
            .image(getImage())
            .storeId(storeId)
            .build();
    }
}
