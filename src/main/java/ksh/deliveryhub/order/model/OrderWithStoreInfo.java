package ksh.deliveryhub.order.model;

import ksh.deliveryhub.order.repository.projection.OrderWithStoreInfoProjection;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderWithStoreInfo {

    private Order order;
    private Store store;

    public static OrderWithStoreInfo from(OrderWithStoreInfoProjection projection) {
        return OrderWithStoreInfo.builder()
            .order(Order.from(projection.getOrderEntity()))
            .store(Store.from(projection.getStoreEntity()))
            .build();
    }
}
