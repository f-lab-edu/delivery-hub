package ksh.deliveryhub.rider.api;

import jakarta.validation.Valid;
import ksh.deliveryhub.rider.dto.request.RiderLoginRequestDto;
import ksh.deliveryhub.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/rider/login")
    public ResponseEntity<Void> login(
        @Valid @RequestBody RiderLoginRequestDto request
    ) {
        riderService.login(request.toModel());

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }
}
