package ksh.deliveryhub.common.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLockService {

    void acquire(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Runnable businessLogic);
}
