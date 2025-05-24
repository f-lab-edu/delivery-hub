package ksh.deliveryhub.coupon.producer;

import ksh.deliveryhub.coupon.dto.event.UserCouponRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCouponRegisterProducer {

    private final String TOPIC_NAME = "coupon-register";

    private final KafkaTemplate<String, UserCouponRegisterEvent> kafkaTemplate;

    public void register(long couponId, long userId, int duration) {
        UserCouponRegisterEvent registerEvent = new UserCouponRegisterEvent(couponId, userId, duration);
        kafkaTemplate.send(TOPIC_NAME, registerEvent);
        log.info("메세지 발행 성공");
    }
}
