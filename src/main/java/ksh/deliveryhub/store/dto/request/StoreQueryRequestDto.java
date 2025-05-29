package ksh.deliveryhub.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.model.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreQueryRequestDto {

    @NotNull(message = "음식 카테고리는 필수입니다.")
    private FoodCategory foodCategory;

    @NotBlank(message = "현재 위치한 시는 필수입니다.")
    private String city;

    @NotBlank(message = "현재 위치한 구는 필수입니다.")
    private String district;

    @NotBlank(message = "현재 위치한 동은 필수입니다.")
    private String subdistrict;

    public Location getLocation() {
        return Location.of(city, district, subdistrict);
    }
}
