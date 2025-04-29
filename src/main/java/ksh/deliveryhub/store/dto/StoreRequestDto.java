package ksh.deliveryhub.store.dto;

import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreRequestDto {
    private FoodCategory foodCategory;
    private String address;
}
