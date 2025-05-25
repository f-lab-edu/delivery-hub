package ksh.deliveryhub.common.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheKey {

    REGISTERED_USER_SET("coupon:%s:registered:user:set");

    private final String key;

    public String makeKey(String... args) {
        return String.format(key, args);
    }
}
