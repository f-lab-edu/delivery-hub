package ksh.deliveryhub.order.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class OrderItemWithMenuProjection {

    private OrderItemEntity orderItemEntity;
    private MenuEntity menuEntity;
}
