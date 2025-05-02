package ksh.deliveryhub.common.util;

import java.util.regex.Pattern;

public class PhoneNumberUtils {

    private static final String PHONE_NUMBER_REGEX = "\\D+";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

    public static String trim(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).replaceAll("");
    }
}
