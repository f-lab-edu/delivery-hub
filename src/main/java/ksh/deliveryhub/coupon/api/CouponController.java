package ksh.deliveryhub.coupon.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.coupon.dto.request.CouponCreateRequestDto;
import ksh.deliveryhub.coupon.dto.response.CouponResponseDto;
import ksh.deliveryhub.coupon.facade.CouponFacade;
import ksh.deliveryhub.coupon.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponFacade couponFacade;

    @PostMapping("/coupons")
    public ResponseEntity<SuccessResponseDto> createCoupon(
        @Valid @RequestBody CouponCreateRequestDto request
    ) {
        Coupon coupon = couponFacade.createCoupon(request.toModel());
        CouponResponseDto couponResponseDto = CouponResponseDto.from(coupon);
        SuccessResponseDto<CouponResponseDto> response = SuccessResponseDto.of(couponResponseDto);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}
