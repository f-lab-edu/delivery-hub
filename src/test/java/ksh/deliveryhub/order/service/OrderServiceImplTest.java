package ksh.deliveryhub.order.service;

import ksh.deliveryhub.common.dto.request.PageRequestDto;
import ksh.deliveryhub.common.dto.response.PageResult;
import ksh.deliveryhub.order.dto.query.AcceptedOrderQuery;
import ksh.deliveryhub.order.entity.OrderEntity;
import ksh.deliveryhub.order.entity.OrderStatus;
import ksh.deliveryhub.order.model.OrderWithStoreInfo;
import ksh.deliveryhub.order.repository.OrderRepository;
import ksh.deliveryhub.rider.entity.Location;
import ksh.deliveryhub.store.entity.Address;
import ksh.deliveryhub.store.entity.StoreEntity;
import ksh.deliveryhub.store.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @Test
    public void 현재_위치와_시_구가_같은_접수된_주문을_조회한다() throws Exception {
        //given
        //첫번째 가게
        Address address1 = Address.of("서울시", "강서구", "방화동", "1로", "1동");
        StoreEntity storeEntity1 = createStoreEntity("가게1", "010-1234-5678", address1);

        //두번째 가게
        Address address2 = Address.of("서울시", "양천구", "목동", "2로", "2동");
        StoreEntity storeEntity2 = createStoreEntity("가게2", "010-1234-5322", address2);
        storeRepository.saveAll(List.of(storeEntity1, storeEntity2));

        //조회 대상 주문
        OrderEntity orderEntity1 = createOrderEntity(OrderStatus.ACCEPTED, storeEntity1.getId());
        OrderEntity orderEntity2 = createOrderEntity(OrderStatus.ACCEPTED, storeEntity1.getId());
        OrderEntity orderEntity3 = createOrderEntity(OrderStatus.ACCEPTED, storeEntity1.getId());

        //위치가 다른 주문
        OrderEntity differentLocationOrder = createOrderEntity(OrderStatus.ACCEPTED, storeEntity2.getId());

        //상태가 다른 주문
        OrderEntity differentStatusOrder = createOrderEntity(OrderStatus.PAID, storeEntity1.getId());
        orderRepository.saveAll(List.of(orderEntity1, orderEntity2, orderEntity3, differentStatusOrder, differentLocationOrder));

        Location location = Location.of("서울시", "강서구");
        PageRequestDto pageRequest = new PageRequestDto(0, 2);
        AcceptedOrderQuery query = AcceptedOrderQuery.builder()
            .location(location)
            .lastCreatedAt(orderEntity1.getCreatedAt().minusMinutes(1))
            .build();

        //given
        PageResult<OrderWithStoreInfo> result = orderService.findOrdersWaitingForDelivery(query, pageRequest);

        //then
        assertThat(result.hasNext()).isTrue();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getOrder().getId()).isEqualTo(orderEntity1.getId());
        assertThat(result.getContent().get(1).getOrder().getId()).isEqualTo(orderEntity2.getId());
    }

    private OrderEntity createOrderEntity(OrderStatus orderStatus, Long storeId) {
        return OrderEntity.builder()
            .orderStatus(orderStatus)
            .storeId(storeId)
            .build();
    }

    private StoreEntity createStoreEntity(String name, String phone, Address address) {
        return StoreEntity.builder()
            .name(name)
            .phone(phone)
            .address(address)
            .build();
    }
}
