package ksh.deliveryhub.point.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.point.entity.UserPointEntity;
import ksh.deliveryhub.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointServiceImpl implements UserPointService{

    private final UserPointRepository userPointRepository;

    @Override
    public void checkBalance(long userId, int pointToUse) {
        UserPointEntity userPointEntity = userPointRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_POINT_NOT_FOUND));

        if(userPointEntity.getBalance() < pointToUse) {
            throw new CustomException(ErrorCode.USER_POINT_NOT_ENOUGH);
        }
    }
}
