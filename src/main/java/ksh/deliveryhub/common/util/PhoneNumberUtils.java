package ksh.deliveryhub.common.util;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;

import java.util.regex.Pattern;

public class PhoneNumberUtils {

    private static final String PHONE_NUMBER_REGEX = "\\D+";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    public static String trim(String phone) {
        String trimmedPhone = PHONE_NUMBER_PATTERN.matcher(phone).replaceAll("");

        if(trimmedPhone.length() != 11) {
            throw new CustomException(ErrorCode.STORE_INVALID_PHONE);
        }

        return trimmedPhone;
    }
}
