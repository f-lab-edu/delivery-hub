package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.store.dto.request.StoreRequestDto;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    @Test
    public void 가게_주소가_주문자의_주소와_일치하고_요청한_카테고리의_음식을_파는_영업_중인_가게를_조회한다() throws Exception{
        //given
        StoreEntity targetStore1 = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, true);
        StoreEntity targetStore2 = createStoreEntity("가게2", "서울시 강서구", FoodCategory.PIZZA, true);
        StoreEntity targetStore3 = createStoreEntity("가게3", "서울시 강서구", FoodCategory.PIZZA, true);

        StoreEntity wrongAddressStore = createStoreEntity("가게4", "고양시 덕양구", FoodCategory.PIZZA, true);
        StoreEntity wrongCategoryStore = createStoreEntity("가게5", "서울시 강서구", FoodCategory.CHICKEN, true);
        StoreEntity closedStore = createStoreEntity("가게6", "서울시 강서구", FoodCategory.PIZZA, false);
        storeRepository.saveAll(List.of(targetStore1, targetStore2, targetStore3, wrongAddressStore, wrongCategoryStore, closedStore));

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
            .foodCategory(FoodCategory.PIZZA)
            .address("서울시 강서구")
            .build();

        PageRequestDto pageRequest = new PageRequestDto(0, 3);

        //when
        PageResult<Store> storePage = storeService.findOpenStores(storeRequestDto, pageRequest);

        //then
        assertThat(storePage.isHasNext()).isFalse();
        assertThat(storePage.getContent())
            .hasSize(3)
            .extracting("name")
            .containsExactly(
                targetStore1.getName(),
                targetStore2.getName(),
                targetStore3.getName()
            );
    }

    private static StoreEntity createStoreEntity(String name, String address, FoodCategory foodCategory, boolean isOpen) {
        return StoreEntity.builder()
            .name(name)
            .description("음식점")
            .address(address)
            .phone("010-1234-5678")
            .foodCategory(foodCategory)
            .isOpen(isOpen)
            .ownerId(1L)
            .build();
    }
}
