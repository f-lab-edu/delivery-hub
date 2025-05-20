package ksh.deliveryhub.common.lock;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonDistributedLockService implements DistributedLockService{

    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;

    @Override
    public void acquire(
        String key,
        long waitTime,
        long leaseTime,
        TimeUnit unit,
        Runnable businessLogic
    ) {
        String lockKey = LOCK_PREFIX + key;
        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = tryLock(waitTime, leaseTime, unit, lock);

        if (!acquired) {
            throw new CustomException(ErrorCode.LOCK_ACQUIRE_TIMEOUT);
        }

        try {
            businessLogic.run();
        } finally {
            unlock(lock);
        }

    }

    private static boolean tryLock(long waitTime, long leaseTime, TimeUnit unit, RLock lock) {
        boolean acquired;
        try {
            acquired = lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.LOCK_ACQUIRE_INTERRUPTED);
        }
        return acquired;
    }

    private static void unlock(RLock lock) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    lock.unlock();
                }
            });
        } else {
            lock.unlock();
        }
    }
}
