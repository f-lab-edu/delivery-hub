package ksh.deliveryhub.order.repository.projection;

import com.querydsl.core.annotations.QueryProjection;
import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor = @__(@QueryProjection))
public class OrderWithStoreInfoProjection {

    private OrderEntity orderEntity;
    private StoreEntity storeEntity;
}
