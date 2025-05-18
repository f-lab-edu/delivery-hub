package ksh.deliveryhub.cart.dto.response;

import ksh.deliveryhub.cart.model.CartMenu;
import ksh.deliveryhub.cart.model.CartMenuDetail;
import ksh.deliveryhub.menu.model.Menu;
import ksh.deliveryhub.menu.model.MenuOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartMenuResponseDto {

    private long id;
    private int totalPrice;
    private int quantity;
    private String menuName;
    private String menuDescription;
    private int menuPrice;
    private String menuImage;
    private String optionName;
    private Integer optionPrice;

    public static CartMenuResponseDto from(CartMenuDetail cartMenuDetail) {
        CartMenu cartMenu = cartMenuDetail.getCartMenu();
        Menu menu = cartMenuDetail.getMenu();
        MenuOption menuOption = cartMenuDetail.getMenuOption();

        int quantity = cartMenu.getQuantity();
        int cartMenuPrice = menu.getPrice() + (menuOption != null ? menuOption.getPrice() : 0);
        int totalPrice = quantity * cartMenuPrice;

        return CartMenuResponseDto.builder()
            .id(cartMenu.getId())
            .totalPrice(totalPrice)
            .quantity(cartMenu.getQuantity())
            .menuName(menu.getName())
            .menuDescription(menu.getDescription())
            .menuPrice(menu.getPrice())
            .menuImage(menu.getImage())
            .optionName(menuOption != null ? menuOption.getName() : null)
            .optionPrice(menuOption != null ? menuOption.getPrice() : null)
            .build();
    }
}
