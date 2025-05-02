package ksh.deliveryhub.store.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.common.exception.ErrorCode;
import ksh.deliveryhub.store.dto.request.StoreCreateRequestDto;
import ksh.deliveryhub.store.dto.request.StoreUpdateRequestDto;
import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.entity.StoreStatus;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static ksh.deliveryhub.store.entity.StoreStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        storeRepository.deleteAllInBatch();
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회하고_성공하면_200_응답을_받는다() throws Exception {
        //given
        StoreEntity targetStore1 = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore2 = createStoreEntity("가게2", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        StoreEntity targetStore3 = createStoreEntity("가게3", "서울시 강서구", FoodCategory.PIZZA, OPEN);

        StoreEntity wrongAddressStore = createStoreEntity("가게4", "고양시 덕양구", FoodCategory.PIZZA, OPEN);
        StoreEntity wrongCategoryStore = createStoreEntity("가게5", "서울시 강서구", FoodCategory.CHICKEN, OPEN);
        StoreEntity closedStore = createStoreEntity("가게6", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        storeRepository.saveAll(List.of(targetStore1, targetStore2, targetStore3, wrongAddressStore, wrongCategoryStore, closedStore));

        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("page", "0")
                    .param("size", "3")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.hasNext").value(false))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content[0].name").value(targetStore1.getName()))
            .andExpect(jsonPath("$.data.content[1].name").value(targetStore2.getName()))
            .andExpect(jsonPath("$.data.content[2].name").value(targetStore3.getName()));
    }

    @Test
    public void 새로운_가게를_등록하고_성공하면_201_응답을_받는다() throws Exception {
        //given
        StoreCreateRequestDto request = StoreCreateRequestDto.builder()
            .name("음식점")
            .description("맛있는 음식점")
            .address("서울시")
            .phone("010-1234-5678")
            .foodCategory(FoodCategory.PIZZA)
            .ownerId(1L)
            .build();

        //when //then
        mockMvc.perform(
                post("/stores")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.name").value(request.getName()))
            .andExpect(jsonPath("$.data.address").value(request.getAddress()))
            .andExpect(jsonPath("$.data.foodCategory").value(request.getFoodCategory().name()));
    }

    @Test
    public void 가게_정보를_업데이트_하고_성공하면_200_응답을_받는다() throws Exception {
        //given
        StoreEntity storeEntity = createStoreEntity("변경 전 이름", "변경 전 주소", FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        StoreUpdateRequestDto request = StoreUpdateRequestDto.builder()
            .id(storeEntity.getId())
            .name("변경 후 이름")
            .description("맛있는 음식점")
            .address("변경 후 주소")
            .phone("010-9876-5432")
            .build();

        //when //then
        mockMvc.perform(
                post("/stores/" + storeEntity.getId())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value(request.getName()))
            .andExpect(jsonPath("$.data.description").value(request.getDescription()))
            .andExpect(jsonPath("$.data.address").value(request.getAddress()))
            .andExpect(jsonPath("$.data.phone").value(request.getPhone()));
    }

    @Test
    public void 존재하지_않는_가게_정보를_업데이트_하면_404_응답을_받는다() throws Exception {
        //given
        StoreUpdateRequestDto request = StoreUpdateRequestDto.builder()
            .id(4685415L)
            .name("변경 후 이름")
            .description("맛있는 음식점")
            .address("변경 후 주소")
            .phone("010-9876-5432")
            .build();

        //when //then
        mockMvc.perform(
                post("/stores/" + 4685415L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.code").value(ErrorCode.STORE_NOT_FOUND.name()));
    }

    @Test
    public void 가게_오픈_상태를_업데이트_하고_성공하면_200_응답을_받는다() throws Exception {
        //given
        StoreEntity storeEntity = createStoreEntity("음식점", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/status", storeEntity.getId())
                    .param("isOpen", "false")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.open").value(false));
    }

    @Test
    public void 가게_오픈_상태를_업데이트_할_때_오픈_상태를_누락하면_400_응답을_받는다() throws Exception {
        //given
        StoreEntity storeEntity = createStoreEntity("음식점", "서울시 강서구", FoodCategory.PIZZA, OPEN);
        storeRepository.save(storeEntity);

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/status", storeEntity.getId())
            ).andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void 존재하지_않는_가게의_오픈_상태를_업데이트_하려_하면_404_응답을_받는다() throws Exception {
        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/status", 116515L)
            ).andDo(print())
            .andExpect(status().isBadRequest());
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
