package ksh.deliveryhub.point.service;

public interface UserPointService {

    void checkBalanceBeforeOrder(long userId, int pointToUse);

    void usePoint(long userId, int pointToUse);

    int earnPoint(long userId, int finalPrice);
}
