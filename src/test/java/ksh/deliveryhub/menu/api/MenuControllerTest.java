package ksh.deliveryhub.menu.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksh.deliveryhub.menu.dto.request.MenuCreateRequestDto;
import ksh.deliveryhub.menu.dto.request.MenuOptionCreateRequestDto;
import ksh.deliveryhub.menu.dto.request.MenuOptionUpdateRequestDto;
import ksh.deliveryhub.menu.dto.request.MenuUpdateRequestDto;
import ksh.deliveryhub.menu.entity.MenuEntity;
import ksh.deliveryhub.menu.entity.MenuOptionEntity;
import ksh.deliveryhub.menu.entity.MenuStatus;
import ksh.deliveryhub.menu.repository.MenuOptionRepository;
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

import java.util.List;

import static ksh.deliveryhub.menu.entity.MenuStatus.AVAILABLE;
import static ksh.deliveryhub.menu.entity.MenuStatus.UNAVAILABLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuOptionRepository menuOptionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        menuRepository.deleteAllInBatch();
    }

    @Test
    public void 새로운_메뉴를_등록에_성공하면_초기_상태를_이용_불가로_설정하고_201응답을_받는다() throws Exception {
        //given
        StoreEntity store = createStore();
        storeRepository.save(store);

        List<MenuOptionCreateRequestDto> options = List.of(
            createMenuOption("치즈 추가", 1000),
            createMenuOption("소스 추가", 100)
        );

        MenuCreateRequestDto request = createMenuCreateRequestDto(
            "페퍼로니 피자", "맛있음", 1000, "url", options
        );

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/menus", store.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.name").value(request.getName()))
            .andExpect(jsonPath("$.data.description").value(request.getDescription()))
            .andExpect(jsonPath("$.data.price").value(request.getPrice()))
            .andExpect(jsonPath("$.data.image").value(request.getImage()))
            .andExpect(jsonPath("$.data.status").value(UNAVAILABLE.name()));
    }

    @Test
    public void 새로운_메뉴를_등록할_때_초기_없는_가게_id를_보내면_404응답을_받는다() throws Exception {
        //given
        MenuCreateRequestDto request = createMenuCreateRequestDto(
            "페퍼로니 피자", "맛있음", 1000, "url", List.of()
        );

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

    @Test
    public void 메뉴의_정보_업데이트를_성공하면_200응답을_받는다() throws Exception {
        //given
        StoreEntity store = createStore();
        storeRepository.save(store);

        MenuEntity menu = createMenuEntity(
            "피자", "맛있음", UNAVAILABLE, 1522, "url", store.getId()
        );
        menuRepository.save(menu);

        MenuOptionEntity option1 = createMenuOption("치즈 추가", 1000, menu.getId());
        MenuOptionEntity option2 = createMenuOption("소스 추가", 1000, menu.getId());
        menuOptionRepository.saveAll(List.of(option1, option2));

        List<MenuOptionUpdateRequestDto> updateOptions = List.of(
            createMenuOptionUpdateRequestDto(option1.getId(), "치즈 더 추가", 1000),
            createMenuOptionUpdateRequestDto(option2.getId(), "소스 추가", 100)
        );

        MenuUpdateRequestDto request = createMenuUpdateRequestDto(
            "피자", "더 맛있어짐", AVAILABLE, 1000, "url", updateOptions
        );

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/menus/{menuId}", store.getId(), menu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.description").value(request.getDescription()))
            .andExpect(jsonPath("$.data.status").value(request.getMenuStatus().name()))
            .andExpect(jsonPath("$.data.price").value(request.getPrice()))
            .andExpect(jsonPath("$.data.options").isArray())
            .andExpect(jsonPath("$.data.options[0].name").value("치즈 더 추가"))
            .andExpect(jsonPath("$.data.options[1].price").value(100));
    }

    @Test
    public void 메뉴가_속한_가게_정보가_일치하지_않으면_403응답을_받는다() throws Exception {
        //given
        StoreEntity store = createStore();
        storeRepository.save(store);

        MenuEntity menu = createMenuEntity(
            "피자", "맛있음", UNAVAILABLE, 1522, "url", 16165L
        );
        menuRepository.save(menu);

        MenuUpdateRequestDto request = createMenuUpdateRequestDto(
            "피자", "더 맛있음", AVAILABLE, 1000, "url", List.of()
        );

        //when //then
        mockMvc.perform(
                post("/stores/{storeId}/menus/{menuId}", store.getId(), menu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(403))
            .andExpect(jsonPath("$.code").value("MENU_STORE_ID_MISMATCH"));
    }

    @Test
    public void 없는_메뉴의_정보_업데이트를_하면_404응답을_받는다() throws Exception {
        StoreEntity store = createStore();
        storeRepository.save(store);

        MenuEntity menuEntity = createMenuEntity("피자", "맛있음", UNAVAILABLE, 150, "url", store.getId());
        menuRepository.save(menuEntity);

        MenuUpdateRequestDto request = createMenuUpdateRequestDto(
            "피자", "더 맛있음", AVAILABLE, 1000, "url", List.of()
        );

        mockMvc.perform(
                post("/stores/{storeId}/menus/{menuId}", store.getId(), 61651651)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.code").value("MENU_NOT_FOUND"));
    }

    private StoreEntity createStore() {
        return StoreEntity.builder()
            .build();
    }

    private MenuEntity createMenuEntity(
        String name,
        String description,
        MenuStatus status,
        int price,
        String image,
        long storeId
    ) {
        return MenuEntity.builder()
            .name(name)
            .description(description)
            .menuStatus(status)
            .price(price)
            .image(image)
            .storeId(storeId)
            .build();
    }

    private MenuCreateRequestDto createMenuCreateRequestDto(
        String name,
        String description,
        int price,
        String image,
        List<MenuOptionCreateRequestDto> options
    ) {
        return MenuCreateRequestDto.builder()
            .name(name)
            .description(description)
            .price(price)
            .image(image)
            .menuOptions(options)
            .build();
    }

    private MenuUpdateRequestDto createMenuUpdateRequestDto(
        String name,
        String description,
        MenuStatus status,
        int price,
        String image,
        List<MenuOptionUpdateRequestDto> options
    ) {
        return MenuUpdateRequestDto.builder()
            .name(name)
            .description(description)
            .menuStatus(status)
            .price(price)
            .image(image)
            .menuOptions(options)
            .build();
    }

    private static MenuOptionCreateRequestDto createMenuOption(
        String name,
        int price
    ) {
        return MenuOptionCreateRequestDto.builder()
            .name(name)
            .price(price)
            .build();
    }

    private static MenuOptionUpdateRequestDto createMenuOptionUpdateRequestDto(
        Long id,
        String name,
        int price
    ) {
        return MenuOptionUpdateRequestDto.builder()
            .id(id)
            .name(name)
            .price(price)
            .build();
    }

    private static MenuOptionEntity createMenuOption(
        String name,
        int price,
        long menuId
    ) {
        return MenuOptionEntity.builder()
            .name(name)
            .price(price)
            .menuId(menuId)
            .build();
    }
}
