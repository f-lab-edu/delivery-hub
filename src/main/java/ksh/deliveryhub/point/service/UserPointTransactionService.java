package ksh.deliveryhub.point.service;

import ksh.deliveryhub.point.model.UserPointTransaction;

public interface UserPointTransactionService {

    UserPointTransaction saveUseTransaction(int amountToUse, long orderId, long userPointId);

    void saveEarnTransaction(int earnedPoint, long orderId, long userPointId);
}
