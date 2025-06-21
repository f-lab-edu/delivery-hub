package ksh.deliveryhub.order.dto.query;

import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.rider.entity.Location;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AcceptedOrderQuery {

    private OrderStatus status;
    private Location location;
    private LocalDateTime lastCreatedAt;
}
