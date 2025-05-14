package ksh.deliveryhub.point.service;

import ksh.deliveryhub.point.model.UserPointTransaction;

public interface UserPointTransactionService {

    UserPointTransaction saveUseTransaction(int amountToUse, long orderId, long userPointId);
}
