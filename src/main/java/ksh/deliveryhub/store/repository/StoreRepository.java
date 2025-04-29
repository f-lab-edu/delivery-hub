package ksh.deliveryhub.store.repository;

import ksh.deliveryhub.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long>, StoreQueryRepository {
}
