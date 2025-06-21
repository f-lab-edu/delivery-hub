package ksh.deliveryhub.order.dto.query;

import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.store.entity.Coordinate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WaitingForRiderOrderQuery {

    private OrderStatus status;
    private Coordinate coordinate;
    private int radius;
}
