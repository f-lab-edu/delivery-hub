package ksh.deliveryhub.cart.model;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.menu.entity.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class CartMenuDetail {

    private Long id;
    private Integer quantity;

    private Long menuId;
    private String menuName;
    private String menuDescription;
    private MenuStatus menuStatus;
    private Integer menuPrice;
    private String image;
    private Long storeId;

    private Long optionId;
    private String optionName;
    private Integer optionPrice;

    public int getTotalPrice() {
        int optionPrice = optionId != null ? this.optionPrice : 0;
        return (menuPrice + optionPrice) * quantity;
    }
}
