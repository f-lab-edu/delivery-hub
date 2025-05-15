package ksh.deliveryhub.point.repository;

import ksh.deliveryhub.point.entity.UserPointTransactionEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserPointTransactionQueryRepository {

    List<UserPointTransactionEntity> findAvailableEarningTransactions(long userId, LocalDate now);
}
