package ksh.deliveryhub.point.service;

import ksh.deliveryhub.point.entity.PointEventType;
import ksh.deliveryhub.point.entity.UserPointTransactionEntity;
import ksh.deliveryhub.point.model.UserPointTransaction;
import ksh.deliveryhub.point.repository.UserPointTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static ksh.deliveryhub.point.entity.PointEventType.EARN;
import static ksh.deliveryhub.point.entity.PointEventType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@SuppressWarnings("removal")
class UserPointTransactionServiceImplTest {

    @Autowired
    UserPointTransactionService userPointTransactionService;

    @Autowired
    UserPointTransactionRepository userPointTransactionRepository;

    @MockBean
    Clock clock;

    @AfterEach
    void tearDown() {
        userPointTransactionRepository.deleteAllInBatch();
    }

    @Test
    public void 포인트_사용_시_만료_임박_순으로_차감하고_사용로그를_저장한다() throws Exception{
        //given
        //사용 가능한 포인트 기록 2개 저장
        LocalDate expireDate1 = LocalDate.of(2021, 1, 2);
        UserPointTransactionEntity availableUseTx1 = createUserPointTransactionEntity(EARN, 1000, 100, expireDate1, 1L);

        LocalDate expireDate2 = LocalDate.of(2021, 1, 2);
        UserPointTransactionEntity availableUseTx2 = createUserPointTransactionEntity(EARN, 1000, 1000, expireDate2, 1L);

        //사용 불가능한 포인트 기록 저장
        LocalDate expireDate3 = LocalDate.of(2020, 12, 31);
        UserPointTransactionEntity expiredUseTx = createUserPointTransactionEntity(EARN, 2000, 2000, expireDate3, 1L);
        userPointTransactionRepository.saveAll(List.of(availableUseTx1, availableUseTx2, expiredUseTx));

        Instant fixedInstant = Instant.parse("2021-01-01T00:05:15Z");
        ZoneId zone = ZoneId.of("Asia/Seoul");
        given(clock.instant()).willReturn(fixedInstant);
        given(clock.getZone()).willReturn(zone);

        //when
        UserPointTransaction userPointTransaction = userPointTransactionService.saveUseTransaction(700, 1L, 1L);

        //then
        UserPointTransactionEntity refreshedAvailableTx1 = userPointTransactionRepository
            .findById(availableUseTx1.getId()).get();
        assertThat(refreshedAvailableTx1.getRemainingBalance()).isZero();

        UserPointTransactionEntity refreshedAvailableTx2 = userPointTransactionRepository
            .findById(availableUseTx2.getId()).get();
        assertThat(refreshedAvailableTx2.getRemainingBalance()).isEqualTo(400);

        UserPointTransactionEntity refreshedExpiredTx = userPointTransactionRepository
            .findById(expiredUseTx.getId()).get();
        assertThat(refreshedExpiredTx.getRemainingBalance()).isEqualTo(2000);

        assertThat(userPointTransaction).extracting("pointEventType", "initialBalance")
            .containsExactly(USE, 700);
    }

    private UserPointTransactionEntity createUserPointTransactionEntity(
        PointEventType type,
        Integer initialBalance,
        Integer remainingBalance,
        LocalDate expireDate,
        Long userId
    ) {
        return UserPointTransactionEntity.builder()
            .pointEventType(type)
            .initialBalance(initialBalance)
            .remainingBalance(remainingBalance)
            .expireDate(expireDate)
            .userId(userId)
            .build();
    }
}
