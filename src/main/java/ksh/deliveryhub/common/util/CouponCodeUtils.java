package ksh.deliveryhub.common.util;

import java.util.UUID;

public class CouponCodeUtils {

    public static String generateCode() {
        String uuid = UUID.randomUUID().toString();

        return uuid.replace("-", "")
            .substring(0, 12)
            .toUpperCase();
    }
}
