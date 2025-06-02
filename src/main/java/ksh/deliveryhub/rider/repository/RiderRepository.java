package ksh.deliveryhub.rider.repository;

import ksh.deliveryhub.rider.entity.RiderEntity;
import ksh.deliveryhub.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiderRepository extends JpaRepository<UserEntity, Long> {

    Optional<RiderEntity> findByEmailAndPassword(String email, String password);
}
