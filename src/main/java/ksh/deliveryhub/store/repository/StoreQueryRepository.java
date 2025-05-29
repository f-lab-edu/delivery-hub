package ksh.deliveryhub.store.repository;

import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreQueryRepository {

    Page<StoreEntity> findOpenStores(Location location, FoodCategory foodCategory, Pageable pageable);
}
