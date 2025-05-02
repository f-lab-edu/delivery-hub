package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.store.dto.request.StoreQueryRequestDto;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.model.Store;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static ksh.deliveryhub.store.entity.StoreStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    StoreServiceImpl storeService;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        storeRepository.deleteAllInBatch();
    }

    @Test
    public void 요청한_가게_정보로_가게를_등록한다() throws Exception {
        //given
        Store store = Store.builder()
            .name("음식점")
            .address("서울시")
            .foodCategory(FoodCategory.PIZZA)
            .build();

        //when
        Store savedStore = storeService.registerStore(store);
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(store);
    }

    @Test
    public void 가게_주소가_주문자의_주소와_일치하고_요청한_카테고리의_음식을_파는_영업_중인_가게를_조회한다() throws Exception {
        //given
        StoreEntity targetStore1 = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore2 = createStoreEntity("가게2", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore3 = createStoreEntity("가게3", "서울시 강서구", FoodCategory.PIZZA, OPEN);

        StoreEntity wrongAddressStore = createStoreEntity("가게4", "고양시 덕양구", FoodCategory.PIZZA, OPEN);
        StoreEntity wrongCategoryStore = createStoreEntity("가게5", "서울시 강서구", FoodCategory.CHICKEN, OPEN);
        StoreEntity closedStore = createStoreEntity("가게6", "서울시 강서구", FoodCategory.PIZZA, CLOSED);
        storeRepository.saveAll(List.of(targetStore1, targetStore2, targetStore3, wrongAddressStore, wrongCategoryStore, closedStore));

        StoreQueryRequestDto storeRequestDto = StoreQueryRequestDto.builder()
            .foodCategory(FoodCategory.PIZZA)
            .address("서울시 강서구")
            .build();

        PageRequestDto pageRequest = new PageRequestDto(0, 3);

        //when
        PageResult<Store> storePage = storeService.findOpenStores(storeRequestDto.toModel(), pageRequest);

        //then
        assertThat(storePage.hasNext()).isFalse();
        assertThat(storePage.getContent())
            .hasSize(3)
            .extracting("name")
            .containsExactly(
                targetStore1.getName(),
                targetStore2.getName(),
                targetStore3.getName()
            );
    }

    @Test
    public void 요청한_가게_정보로_가게_정보를_업데이트한다() throws Exception {
        //given
        StoreEntity storeEntity = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        Store storeUpdateInfo = Store.builder()
            .id(storeEntity.getId())
            .name("피자집")
            .description("맛있는 피자집")
            .address("서울시 양천구")
            .phone("010-9876-5432")
            .foodCategory(FoodCategory.PIZZA)
            .status(OPEN)
            .ownerId(1L)
            .build();

        //when
        Store store = storeService.updateStore(storeUpdateInfo);

        //then
        assertThat(storeUpdateInfo)
            .usingRecursiveComparison()
            .isEqualTo(store);
    }

    @Test
    public void 잘못된_가게_id를_요청하면_가게_정보_업데이트_시_예외가_발생한다() throws Exception {
        //given
        StoreEntity storeEntity = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        Store storeUpdateInfo = Store.builder()
            .id(15557L)
            .name("피자집")
            .description("맛있는 피자집")
            .address("서울시 양천구")
            .phone("010-9876-5432")
            .foodCategory(FoodCategory.PIZZA)
            .status(OPEN)
            .ownerId(1L)
            .build();

        //when //then
        assertThatExceptionOfType(CustomException.class)
            .isThrownBy(() -> storeService.updateStore(storeUpdateInfo))
            .returns(ErrorCode.STORE_NOT_FOUND, CustomException::getErrorCode);
    }

    private static StoreEntity createStoreEntity(String name, String address, FoodCategory foodCategory, StoreStatus status) {
        return StoreEntity.builder()
            .name(name)
            .description("음식점")
            .address(address)
            .phone("010-1234-5678")
            .foodCategory(foodCategory)
            .status(status)
            .ownerId(1L)
            .build();
    }
}
