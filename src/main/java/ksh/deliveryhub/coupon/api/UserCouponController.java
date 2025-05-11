package ksh.deliveryhub.coupon.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.coupon.dto.request.UserCouponQueryRequestDto;
import ksh.deliveryhub.coupon.dto.response.UserCouponDetailListResponseDto;
import ksh.deliveryhub.coupon.dto.response.UserCouponDetailResponseDto;
import ksh.deliveryhub.coupon.facade.CouponFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponFacade couponFacade;

    @GetMapping("/users/{userId}/coupons")
    public ResponseEntity<SuccessResponseDto> findAvailableCouponDetails(
        @PathVariable("userId") Long userId,
        @Valid UserCouponQueryRequestDto request
    ) {
        List<UserCouponDetailResponseDto> userCouponDetails = couponFacade.findAvailableCouponDetails(userId, request.getFoodCategory())
            .stream()
            .map(UserCouponDetailResponseDto::from)
            .toList();

        UserCouponDetailListResponseDto userCouponDetailList = UserCouponDetailListResponseDto.of(userCouponDetails);
        SuccessResponseDto<UserCouponDetailListResponseDto> response = SuccessResponseDto.of(userCouponDetailList);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PostMapping("/users/{userId}/coupons/{code}")
    public ResponseEntity<SuccessResponseDto> registerCoupon(
        @PathVariable("userId") Long userId,
        @PathVariable("code") String code
    ) {
        couponFacade.registerUserCoupon(userId, code);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }
}
