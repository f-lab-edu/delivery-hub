package ksh.deliveryhub.point.model;

import ksh.deliveryhub.point.entity.PointEventType;
import ksh.deliveryhub.point.entity.UserPointTransactionEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserPointTransaction {

    private Long id;
    private PointEventType pointEventType;
    private Integer initialBalance;
    private Integer remainingBalance;
    private LocalDate expireDate;
    private Long orderId;
    private Long userPointId;

    public static UserPointTransaction from(UserPointTransactionEntity entity) {
        return UserPointTransaction.builder()
            .id(entity.getId())
            .pointEventType(entity.getPointEventType())
            .initialBalance(entity.getInitialBalance())
            .remainingBalance(entity.getRemainingBalance())
            .expireDate(entity.getExpireDate())
            .orderId(entity.getOrderId())
            .userPointId(entity.getUserPointId())
            .build();
    }

    public UserPointTransactionEntity toEntity() {
        return UserPointTransactionEntity.builder()
            .id(getId())
            .pointEventType(getPointEventType())
            .initialBalance(getInitialBalance())
            .remainingBalance(getRemainingBalance())
            .expireDate(getExpireDate())
            .orderId(getOrderId())
            .userPointId(getUserPointId())
            .build();
    }
}
