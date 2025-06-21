package ksh.deliveryhub.order.dto.request;

import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.order.dto.query.WaitingForRiderOrderQuery;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.store.entity.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingForRiderOrderRequestDto {

    @NotNull(message = "현재 라이더가 위치한 위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "현재 라이더가 위치한 경도는 필수입니다.")
    private Double longitude;

    @NotNull(message = "탐색 반경은 필수입니다.")
    private Integer radius;

    public WaitingForRiderOrderQuery toQuery() {
        Coordinate coordinate = Coordinate.of(latitude, longitude);

        return WaitingForRiderOrderQuery.builder()
            .status(OrderStatus.ACCEPTED)
            .coordinate(coordinate)
            .radius(radius)
            .build();
    }
}
