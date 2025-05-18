package ksh.deliveryhub.cart.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.cart.entity.CartMenuEntity;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class CartMenuDetailProjection {

    private CartMenuEntity cartMenuEntity;
    private MenuEntity menuEntity;
    private MenuOptionEntity menuOptionEntity;
    private StoreEntity storeEntity;
}
