package ksh.deliveryhub.store.repository;

import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryRepository {

    Page<StoreEntity> findOpenStores(String address, FoodCategory foodCategory, Pageable pageable);
}
