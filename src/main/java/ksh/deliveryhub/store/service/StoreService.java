package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.store.dto.StoreRequestDto;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public PageResult<Store> findOpenStores(StoreRequestDto request, Pageable pageable) {
        Page<Store> storesPage = storeRepository.findOpenStores(
                request.getAddress(),
                request.getFoodCategory(),
                pageable
            )
            .map(Store::from);

        return PageResult.of(storesPage.hasNext(), storesPage.getContent());
    }
}
