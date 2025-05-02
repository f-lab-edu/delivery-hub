package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.store.model.Store;

public interface StoreService {

    Store registerStore(Store store);

    PageResult<Store> findOpenStores(Store store, PageRequestDto pageRequest);

    Store updateStore(Store store);
}
