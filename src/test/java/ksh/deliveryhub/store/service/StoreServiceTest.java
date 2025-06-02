package ksh.deliveryhub.store.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.common.exception.CustomException;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.dto.request.StoreQueryRequestDto;
import ksh.deliveryhub.store.entity.Address;
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
        Address address = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        Store store = Store.builder()
            .name("음식점")
            .address(address)
            .foodCategory(FoodCategory.PIZZA)
            .phone("010-1234-5678")
            .build();

        //when
        Store savedStore = storeService.registerStore(store);
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(store);
    }

    @Test
    public void 영업_중인_가게를_조회하면_가게_주소가_주문자의_주소와_일치하고_요청한_카테고리의_음식을_파는_가게를_조회한다() throws Exception {
        //given
        Address address1 = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        Address address2 = Address.of("고양시", "덕양구", "예시동", "1로", "1동");
        StoreEntity targetStore1 = createStoreEntity("가게1", address1, FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore2 = createStoreEntity("가게2", address1, FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore3 = createStoreEntity("가게3", address1, FoodCategory.PIZZA, OPEN);

        StoreEntity wrongAddressStore = createStoreEntity("가게4", address2, FoodCategory.PIZZA, OPEN);
        StoreEntity wrongCategoryStore = createStoreEntity("가게5", address1, FoodCategory.CHICKEN, OPEN);
        StoreEntity closedStore = createStoreEntity("가게6", address1, FoodCategory.PIZZA, CLOSED);
        storeRepository.saveAll(List.of(targetStore1, targetStore2, targetStore3, wrongAddressStore, wrongCategoryStore, closedStore));

        Location location = Location.of("서울시", "강서구", "1동");
        PageRequestDto pageRequest = new PageRequestDto(0, 3);

        //when
        PageResult<Store> storePage = storeService.findOpenStores(
            FoodCategory.PIZZA,
            location,
            pageRequest
        );

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
    public void 영업_중인_가게를_조회할_때_페이지_사이즈_보다_남은_데이터가_많으면_haxNext가_true이다() throws Exception {
        //given
        Address address = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        StoreEntity storeEntity1 = createStoreEntity("가게1", address, FoodCategory.PIZZA, OPEN);
        StoreEntity storeEntity2 = createStoreEntity("가게2", address, FoodCategory.PIZZA, OPEN);
        StoreEntity storeEntity3 = createStoreEntity("가게3", address, FoodCategory.PIZZA, OPEN);
        StoreEntity storeEntity4 = createStoreEntity("가게4", address, FoodCategory.PIZZA, OPEN);
        storeRepository.saveAll(List.of(storeEntity1, storeEntity2, storeEntity3, storeEntity4));

        Location location = Location.of("서울시", "강서구", "1동");
        PageRequestDto pageRequest = new PageRequestDto(0, 3);

        //when
        PageResult<Store> storePage = storeService.findOpenStores(
            FoodCategory.PIZZA,
            location,
            pageRequest
        );

        //then
        assertThat(storePage.hasNext()).isTrue();
        assertThat(storePage.getContent())
            .hasSize(3)
            .extracting("name")
            .containsExactly(
                storeEntity1.getName(),
                storeEntity2.getName(),
                storeEntity3.getName()
            );
    }

    @Test
    public void 요청한_가게_정보로_가게_정보를_업데이트한다() throws Exception {
        //given
        Address beforeAddress = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        StoreEntity storeEntity = createStoreEntity("가게1", beforeAddress, FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        Address afterAddress = Address.of("서울시", "양천구", "목동", "1로", "1동");
        Store storeUpdateInfo = Store.builder()
            .id(storeEntity.getId())
            .name("피자집")
            .description("맛있는 피자집")
            .address(afterAddress)
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
        Address beforeAddress = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        StoreEntity storeEntity = createStoreEntity("가게1", beforeAddress, FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        Address afterAddress = Address.of("서울시", "양천구", "목동", "1로", "1동");
        Store storeUpdateInfo = Store.builder()
            .id(15557L)
            .name("피자집")
            .description("맛있는 피자집")
            .address(afterAddress)
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

    private static StoreEntity createStoreEntity(String name, Address address, FoodCategory foodCategory, StoreStatus status) {
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
