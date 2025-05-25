package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.coupon.entity.CouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.model.Coupon;
import ksh.deliveryhub.coupon.model.UserCoupon;
import ksh.deliveryhub.coupon.model.UserCouponDetail;
import ksh.deliveryhub.coupon.producer.UserCouponRegisterProducer;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import ksh.deliveryhub.coupon.repository.projection.UserCouponDetailProjection;
import ksh.deliveryhub.store.entity.FoodCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponRegisterProducer couponRegisterProducer;
    private final Clock clock;

    @Override
    public void registerCoupon(long userId, Coupon coupon) {
        couponRegisterProducer.register(userId, coupon.getId(), coupon.getDuration());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserCouponDetail> findAvailableCouponsWithDetail(long userId, FoodCategory foodCategory) {
        return userCouponRepository.findAvailableCouponsWithDetail(userId, foodCategory).stream()
            .map(projection -> UserCouponDetail.fromEntities(
                projection.getUserCouponEntity(),
                projection.getCouponEntity()
            ))
            .toList();
    }

    @Transactional
    @Override
    public UserCouponDetail reserveCoupon(Long id, long userId, FoodCategory foodCategory) {
        if(id == null) {
            return UserCouponDetail.empty();
        }

        UserCouponDetailProjection userCouponDetailProjection = userCouponRepository.findCouponToApply(id, userId, foodCategory)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_COUPON_NOT_USABLE));

        UserCouponEntity userCouponEntity = userCouponDetailProjection.getUserCouponEntity();
        userCouponEntity.reserve();

        CouponEntity couponEntity = userCouponDetailProjection.getCouponEntity();

        return UserCouponDetail.fromEntities(userCouponEntity, couponEntity);
    }

    @Transactional
    @Override
    public UserCoupon useCoupon(long id, long userId) {
        UserCouponEntity userCouponEntity = userCouponRepository.getReservedCouponForPayment(id, userId);
        userCouponEntity.use();

        return UserCoupon.from(userCouponEntity);
    }
}
