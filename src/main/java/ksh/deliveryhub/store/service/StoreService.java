package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.model.Store;

public interface StoreService {

    Store registerStore(Store store);

    PageResult<Store> findOpenStores(FoodCategory foodCategory, Location location, PageRequestDto pageRequest);

    Store updateStore(Store store);

    void exists(long id);
}
