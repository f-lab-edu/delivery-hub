package ksh.deliveryhub.coupon.service;

import ksh.deliveryhub.coupon.model.CouponTransaction;
import ksh.deliveryhub.coupon.model.UserCoupon;

public interface CouponTransactionService {

    CouponTransaction saveIssueTransaction(UserCoupon userCoupon);

    CouponTransaction saveUseTransaction(UserCoupon userCoupon, long orderId);
}
