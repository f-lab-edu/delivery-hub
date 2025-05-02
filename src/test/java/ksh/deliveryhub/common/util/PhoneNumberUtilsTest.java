package ksh.deliveryhub.common.util;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PhoneNumberUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"010-1234-5678", "(010) 1234.5678", "010.1234.5678", "010 1234 5678"})
    public void 전화번호에서_숫자만_추출하여_반환한다(String phone) throws Exception{
        //given
        String expected = "01012345678";

        //when
        String trimmedPhone = PhoneNumberUtils.trim(phone);

        //then
        assertThat(trimmedPhone).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-1234-567", "010-1234-56754"})
    public void 전화번호에서_숫자만_추출_시_숫자가_11개가_아니면_예외가_발생한다(String phone) throws Exception{
        //when //then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> PhoneNumberUtils.trim(phone))
            .returns(ErrorCode.STORE_INVALID_PHONE, CustomException::getErrorCode);
    }
}
