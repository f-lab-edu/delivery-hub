package ksh.deliveryhub.point.repository;

import ksh.deliveryhub.point.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPointRepository extends JpaRepository<UserPointEntity, Long> {

    Optional<UserPointEntity> findByUserId(long userId);
}
