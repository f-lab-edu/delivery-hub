package ksh.deliveryhub.point.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_point_transaction")
public class UserPointTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PointEventType pointEventType;

    private Integer initialBalance;

    private Integer remainingBalance;

    private LocalDate expireDate;

    private Long orderId;

    private Long userPointId;

    public void decreaseRemainingBalance(int amount) {
        this.remainingBalance -= amount;
    }

    @Builder
    private UserPointTransactionEntity(
        Long id,
        PointEventType pointEventType,
        Integer initialBalance,
        Integer remainingBalance,
        LocalDate expireDate,
        Long orderId,
        Long userPointId
    ) {
        this.id = id;
        this.pointEventType = pointEventType;
        this.initialBalance = initialBalance;
        this.remainingBalance = remainingBalance;
        this.expireDate = expireDate;
        this.orderId = orderId;
        this.userPointId = userPointId;
    }
}
