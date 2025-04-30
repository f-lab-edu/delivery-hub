package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.store.dto.request.StoreRequestDto;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public Store registerStore(Store store) {
        StoreEntity storeEntity = storeRepository.save(store.toEntity());

        return Store.from(storeEntity);
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public Store updateStore(Store store) {
        StoreEntity storeEntity = storeRepository.findById(store.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        storeEntity.update(
            store.getName(),
            store.getDescription(),
            store.getAddress(),
            store.getPhone()
        );

        return Store.from(storeEntity);
    }

    @Transactional
    public Store updateStoreStatus(long storeId, boolean status) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        storeEntity.updateIsOpen(status);

        return Store.from(storeEntity);
    }
}
