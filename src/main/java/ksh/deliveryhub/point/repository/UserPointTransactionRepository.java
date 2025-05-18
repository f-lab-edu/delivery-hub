package ksh.deliveryhub.point.repository;

import ksh.deliveryhub.point.entity.UserPointTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPointTransactionRepository extends JpaRepository<UserPointTransactionEntity, Long>, UserPointTransactionQueryRepository {
}
