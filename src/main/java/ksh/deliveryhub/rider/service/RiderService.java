package ksh.deliveryhub.rider.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.rider.entity.RiderEntity;
import ksh.deliveryhub.rider.entity.RiderStatus;
import ksh.deliveryhub.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;

    public void login(String email, String password) {
        RiderEntity riderEntity = riderRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        riderEntity.updateStatus(RiderStatus.IDLE);
    }
}
