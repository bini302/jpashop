package jpabook.jpashop.Service;

import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    //이게 좋은 테스트는 아님..
    //디비도 없이 스프링도 없이 순순하게 메서드 돌아가는거만 보는 단위테스트가 좀 더 좋음
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("jpaBook", 10000, 10);

        int orderCount=2;

        //when
        Long orderId=orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder=orderRepository.findOne(orderId);
        //"메시지", 기댓값, 실제값
        assertEquals("상품 주문시  상태는 order", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야함",1, getOrder.getOrderItems().size());
        assertEquals("주문 가격을 가격*수량임", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야함", 10, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member=createMember();
        Book item = createBook("jpaBook", 10000, 10);
        int orderCount=2;
        Long orderId= orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder=orderRepository.findOne(orderId);
        assertEquals("it must be CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("canceled orderItem stockQuantity should be rise", 10, item.getStockQuantity());
    }
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member=createMember();
        Item item=createBook("jpaBook", 10000, 10);
        int orderCount=11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외");

        //이거도 이건데 removeStock의 단위 테스트가 사실 중요하지
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("Soeul", "kang", "123-123"));
        em.persist(member);
        return member;
    }
}