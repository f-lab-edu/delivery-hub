package ksh.deliveryhub.menu.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.menu.dto.request.MenuCreateRequestDto;
import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.repository.MenuRepository;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        menuRepository.deleteAllInBatch();
    }

    @Test
    public void 새로운_메뉴를_등록에_성공하면_초기_상태를_이용_불가로_설정하고_201응답을_받는다() throws Exception {
        //given
        StoreEntity storeEntity = StoreEntity.builder().build();
        storeRepository.save(storeEntity);

        MenuCreateRequestDto request = MenuCreateRequestDto.builder()
            .name("페퍼로니 피자")
            .description("맛있음")
            .price(1000)
            .image("이미지 url")
            .build();

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/menus", storeEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.name").value(request.getName()))
            .andExpect(jsonPath("$.data.description").value(request.getDescription()))
            .andExpect(jsonPath("$.data.price").value(request.getPrice()))
            .andExpect(jsonPath("$.data.image").value(request.getImage()))
            .andExpect(jsonPath("$.data.menuStatus").value(MenuStatus.UNAVAILABLE.name()));
    }

    @Test
    public void 새로운_메뉴를_등록할_때_초기_없는_가게_id를_보내면_404응답을_받는다() throws Exception {
        //given
        MenuCreateRequestDto request = MenuCreateRequestDto.builder()
            .name("페퍼로니 피자")
            .description("맛있음")
            .price(1000)
            .image("이미지 url")
            .build();

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/menus", 5454484)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.code").value("STORE_NOT_FOUND"));
    }
}
