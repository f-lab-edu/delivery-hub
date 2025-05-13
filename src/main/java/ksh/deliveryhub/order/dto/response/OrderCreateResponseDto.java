package ksh.deliveryhub.order.dto.response;

import ksh.deliveryhub.order.model.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {

    private long id;
    private int totalPrice;

    public static OrderCreateResponseDto from(Order order) {
        return OrderCreateResponseDto.builder()
            .id(order.getId())
            .totalPrice(order.getFinalPrice())
            .build();
    }
}
