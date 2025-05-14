package ksh.deliveryhub.point.service;

import ksh.deliveryhub.point.entity.PointEventType;
import ksh.deliveryhub.point.entity.UserPointTransactionEntity;
import ksh.deliveryhub.point.model.UserPointTransaction;
import ksh.deliveryhub.point.repository.UserPointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPointTransactionServiceImpl implements UserPointTransactionService{

    private final UserPointTransactionRepository userPointTransactionRepository;
    private final Clock clock;

    @Override
    public UserPointTransaction saveUseTransaction(int amountToUse, long orderId, long userId) {
        LocalDate today = LocalDate.now(clock);

        List<UserPointTransactionEntity> earnings =
            userPointTransactionRepository.findAvailableEarningTransactions(userId, today);

        deductPoints(earnings, amountToUse);

        UserPointTransactionEntity useTx = UserPointTransactionEntity.builder()
            .pointEventType(PointEventType.USE)
            .initialBalance(amountToUse)
            .orderId(orderId)
            .userId(userId)
            .build();
        userPointTransactionRepository.save(useTx);

        return UserPointTransaction.from(useTx);
    }

    @Override
    public void saveEarnTransaction(int earnedPoint, long orderId, long userId) {
        LocalDate expireDate = LocalDate.now(clock).plusMonths(1);

        UserPointTransactionEntity earnedTransaction = UserPointTransactionEntity.builder()
            .pointEventType(PointEventType.EARN)
            .initialBalance(earnedPoint)
            .remainingBalance(earnedPoint)
            .expireDate(expireDate)
            .orderId(orderId)
            .userId(userId)
            .build();
        userPointTransactionRepository.save(earnedTransaction);
    }

    private static void deductPoints(List<UserPointTransactionEntity> earnings, int amountToDeduct) {
        int remaining = amountToDeduct;
        for (UserPointTransactionEntity earn : earnings) {
            if (remaining <= 0) break;

            int deductionAmount = Math.min(earn.getRemainingBalance(), remaining);
            earn.decreaseRemainingBalance(deductionAmount);
            remaining -= deductionAmount;
        }
    }
}
