package ksh.deliveryhub.rider.api;

import ksh.deliveryhub.common.dto.response.SuccessResponseDto;
import ksh.deliveryhub.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/rider/login")
    public ResponseEntity<Void> login(String email, String password) {
        riderService.login(email, password);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
