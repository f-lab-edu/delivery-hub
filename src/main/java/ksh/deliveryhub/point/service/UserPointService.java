package ksh.deliveryhub.point.service;

public interface UserPointService {

    void checkBalanceBeforeOrder(long userId, int pointToUse);
}
