package ksh.deliveryhub.coupon.repository;

public interface UserCouponCacheRepository {

    boolean addRegisteredUserIdInSet(String key, long userId);
}
