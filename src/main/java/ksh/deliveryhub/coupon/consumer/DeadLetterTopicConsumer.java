package ksh.deliveryhub.coupon.consumer;

import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.coupon.dto.event.UserCouponRegisterEvent;
import ksh.deliveryhub.coupon.entity.CouponEventType;
import ksh.deliveryhub.coupon.entity.CouponTransactionEntity;
import ksh.deliveryhub.coupon.entity.UserCouponEntity;
import ksh.deliveryhub.coupon.entity.UserCouponStatus;
import ksh.deliveryhub.coupon.repository.CouponTransactionRepository;
import ksh.deliveryhub.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadLetterTopicConsumer {

    private final UserCouponRepository userCouponRepository;
    private final CouponTransactionRepository couponTransactionRepository;
    private final Clock clock;

    @KafkaListener(
        topics = "coupon-register.DLT",
        groupId = "coupon-register-consumer",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void listen(ConsumerRecord<String, UserCouponRegisterEvent> record) {
        Header header = record.headers().lastHeader("error-message");
        String errorMessage = new String(header.value(), StandardCharsets.UTF_8);
        log.error("쿠폰 발급 과정에서 예외 발생 {}", errorMessage);

        UserCouponRegisterEvent event = record.value();
        UserCouponEntity userCouponEntity = saveUserCouponEntity(event);
        saveUserCouponTransactionLog(userCouponEntity);
    }

    private UserCouponEntity saveUserCouponEntity(UserCouponRegisterEvent event) {
        userCouponRepository.findByUserIdAndCouponId(event.getUserId(), event.getCouponId())
            .ifPresent(userCouponEntity ->
                {throw new CustomException(ErrorCode.USER_COUPON_ALREADY_REGISTERED);}
            );


        int duration = event.getDuration();
        LocalDate expireAt = LocalDate.now(clock).plusDays(duration);

        UserCouponEntity userCouponEntity = UserCouponEntity.builder()
            .couponStatus(UserCouponStatus.ACTIVE)
            .userId(event.getUserId())
            .couponId(event.getCouponId())
            .expireAt(expireAt)
            .build();
        userCouponRepository.save(userCouponEntity);

        return userCouponEntity;
    }

    private void saveUserCouponTransactionLog(UserCouponEntity userCouponEntity) {
        CouponTransactionEntity couponTransactionEntity = CouponTransactionEntity.builder()
            .userCouponId(userCouponEntity.getId())
            .eventType(CouponEventType.ISSUE)
            .build();
        couponTransactionRepository.save(couponTransactionEntity);
    }
}
