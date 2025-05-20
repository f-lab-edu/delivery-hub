package ksh.deliveryhub.common.lock;

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
        boolean acquired;
        try {
            acquired = lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 획득 대기 중 인터럽트 발생", e);
        }

        if (!acquired) {
            throw new IllegalStateException("락을 획득하지 못했습니다: " + lockKey);
        }

        try {
            businessLogic.run();
        } finally {
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
}
