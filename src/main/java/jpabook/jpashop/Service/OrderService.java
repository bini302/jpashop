package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.ItemRepository;
import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.Repository.OrderSearch;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문저장
        orderRepository.save(order);
        //orderItem, delivery는 cascade로 order랑 묶여있어서 이거 하나만 해도댐
        //cascade의 범위는 많이들 고민함
        //참조하는 주인이 명확할 때 (orderItem, delivery는 order만 사용)
        //아니면 private일때
        //위 경우가 아니면 각각 persist 해야댐

        return order.getId();
    }
    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order=orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
    //얘는 전달만 하는거죠? 필요없죠? 고민해보고 괜찮다면 컨트롤러에서 바로 리보지토리로 접근해도됩니다
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
