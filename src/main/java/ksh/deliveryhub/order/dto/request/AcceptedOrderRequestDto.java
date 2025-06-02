package ksh.deliveryhub.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.order.dto.query.AcceptedOrderQuery;
import ksh.deliveryhub.rider.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AcceptedOrderRequestDto {

    @NotBlank(message = "라이더가 현재 위치한 시는 필수입니다. ")
    private String city;

    @NotBlank(message = "라이더가 현재 위치한 구는 필수입니다.")
    private String district;

    @NotNull(message = "이전 페이지 마지막 레코드의 created_at 필드는 필수입니다.")
    private LocalDateTime lastCreatedAt;

    public Location getCurrentLocation() {
        return Location.of(city, district);
    }

    public AcceptedOrderQuery toQuery() {
        return AcceptedOrderQuery.builder()
            .location(getCurrentLocation())
            .lastCreatedAt(lastCreatedAt)
            .build();
    }
}
