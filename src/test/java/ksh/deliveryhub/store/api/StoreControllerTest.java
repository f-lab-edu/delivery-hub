package ksh.deliveryhub.store.api;

import ksh.deliveryhub.store.entity.FoodCategory;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StoreRepository storeRepository;

    @Test
    public void 주문_가능한_가게_리스트를_조회한다() throws Exception {
        //given
        StoreEntity targetStore1 = createStoreEntity("가게1", "서울시 강서구", FoodCategory.PIZZA, true);
        StoreEntity targetStore2 = createStoreEntity("가게2", "서울시 강서구", FoodCategory.PIZZA, true);
        StoreEntity targetStore3 = createStoreEntity("가게3", "서울시 강서구", FoodCategory.PIZZA, true);

        StoreEntity wrongAddressStore = createStoreEntity("가게4", "고양시 덕양구", FoodCategory.PIZZA, true);
        StoreEntity wrongCategoryStore = createStoreEntity("가게5", "서울시 강서구", FoodCategory.CHICKEN, true);
        StoreEntity closedStore = createStoreEntity("가게6", "서울시 강서구", FoodCategory.PIZZA, false);
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
    public void 주문_가능한_가게_리스트를_조회_시_음식_카테고리는_필수이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("address", "서울시 강서구")
                    .param("page", "0")
                    .param("size", "3")
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("음식 카테고리는 필수입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_주소는_필수이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("page", "0")
                    .param("size", "3")
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("주소는 필수입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_페이지_번호는_필수이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("size", "3")
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("페이지 번호는 필수입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_페이지_번호는_0이상이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("page", "-1")
                    .param("size", "3")
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("페이지 번호는 0 이상입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_페이지_크기는_필수이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("page", "0")
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("페이지 크기는 필수입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_페이지_크기는_최대_10이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("page", "0")
                    .param("size", String.valueOf(11) )
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("페이지 크기는 10이하입니다."));
    }

    @Test
    public void 주문_가능한_가게_리스트를_조회_시_페이지_크기는_양수이다() throws Exception {
        //when //then
        mockMvc.perform(
                get("/stores")
                    .param("foodCategory", "PIZZA")
                    .param("address", "서울시 강서구")
                    .param("page", "0")
                    .param("size", String.valueOf(-1) )
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("페이지 크기는 양수입니다."));
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
