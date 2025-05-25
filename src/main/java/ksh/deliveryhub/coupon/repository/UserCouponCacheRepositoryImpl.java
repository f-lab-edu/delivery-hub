package ksh.deliveryhub.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserCouponCacheRepositoryImpl implements UserCouponCacheRepository{

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean addRegisteredUserIdInSet(String key, long userId) {
        Long result = redisTemplate.opsForSet().add(key, userId);
        if(result == 0)
            redisTemplate.expire(key, 1, TimeUnit.HOURS);

        return result == 1;
    }
}
