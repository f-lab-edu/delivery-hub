package ksh.deliveryhub.point.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointTransactionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PointEventType pointEventType;

    private Integer initialBalance;

    private Integer remainingBalance;

    private LocalDateTime expireDate;

    private Long orderId;

    private Long userPointId;

    @Builder
    private UserPointTransactionEntity(
        PointEventType pointEventType,
        Integer initialBalance,
        Integer remainingBalance,
        LocalDateTime expireDate,
        Long orderId,
        Long userPointId
    ) {
        this.pointEventType = pointEventType;
        this.initialBalance = initialBalance;
        this.remainingBalance = remainingBalance;
        this.expireDate = expireDate;
        this.orderId = orderId;
        this.userPointId = userPointId;
    }
}
