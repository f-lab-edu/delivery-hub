package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.store.dto.request.StoreRequestDto;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Store registerStore(Store store) {
        StoreEntity storeEntity = storeRepository.save(store.toEntity());

        return Store.from(storeEntity);
    }

    public PageResult<Store> findOpenStores(StoreRequestDto storeRequest, PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());

        Page<Store> storesPage = storeRepository.findOpenStores(
                storeRequest.getAddress(),
                storeRequest.getFoodCategory(),
                pageable
            )
            .map(Store::from);

        return PageResult.of(storesPage.hasNext(), storesPage.getContent());
    }
}
