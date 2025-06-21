package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.FoodCategory;
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
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;

    @Transactional
    @Override
    public Store registerStore(Store store) {
        StoreEntity storeEntity = storeRepository.save(store.toEntity());

        return Store.from(storeEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResult<Store> findOpenStores(
        FoodCategory foodCategory,
        Location location,
        PageRequestDto pageRequest
    ) {
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());

        Page<Store> storesPage = storeRepository.findOpenStores(
                location,
                foodCategory,
                pageable
            )
            .map(Store::from);

        return PageResult.of(storesPage.hasNext(), storesPage.getContent());
    }

    @Transactional
    @Override
    public Store updateStore(Store store) {
        StoreEntity storeEntity = storeRepository.findById(store.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        storeEntity.update(
            store.getName(),
            store.getDescription(),
            store.getAddress(),
            store.getStatus(),
            store.getPhone()
        );

        return Store.from(storeEntity);
    }

    @Override
    public void exists(long id) {
        storeRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
