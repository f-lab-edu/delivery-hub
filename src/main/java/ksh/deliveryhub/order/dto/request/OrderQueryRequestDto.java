package ksh.deliveryhub.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import ksh.deliveryhub.rider.entity.Location;
import lombok.Getter;

@Getter
public class OrderQueryRequestDto {

    @NotBlank(message = "라이더가 현재 위치한 시는 필수입니다. ")
    private String city;

    @NotBlank(message = "라이더가 현재 위치한 구는 필수입니다.")
    private String district;

    public Location getCurrentLocation() {
        return Location.of(city, district);
    }
}
