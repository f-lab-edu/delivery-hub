package ksh.deliveryhub.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ksh.deliveryhub.point.entity.PointEventType;
import ksh.deliveryhub.point.entity.UserPointTransactionEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static ksh.deliveryhub.point.entity.QUserPointTransactionEntity.userPointTransactionEntity;

@RequiredArgsConstructor
public class UserPointTransactionQueryRepositoryImpl implements UserPointTransactionQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserPointTransactionEntity> findAvailableEarningTransactions(long userPointId, LocalDate now) {
        return queryFactory
            .select(userPointTransactionEntity)
            .from(userPointTransactionEntity)
            .where(
                userPointTransactionEntity.pointEventType.eq(PointEventType.EARN),
                userPointTransactionEntity.userPointId.eq(userPointId),
                userPointTransactionEntity.expireDate.after(LocalDate.now()),
                userPointTransactionEntity.remainingBalance.gt(0)
            )
            .orderBy(userPointTransactionEntity.expireDate.asc())
            .orderBy(userPointTransactionEntity.createdAt.asc())
            .fetch();
    }
}
