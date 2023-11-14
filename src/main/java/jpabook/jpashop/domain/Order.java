package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//이거 안쓰면 Order라는 테이블이 생김
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")    //fk이름은 member_id
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //hibernate가 알아서 LocalDateTime 지원해줌
    private LocalDateTime orderDate;

    //주문 상태 [order, cancel]
    @Enumerated(EnumType.STRING)
    private OrderStatus status;



    //==연관관계 편의 메서드 ==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성 메서드
    //생성 > set이 아니라 생성에서 모든걸 한번에
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem: orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    //주문 취소
    public void cancel(){
        //체크 로직이 엔티티 안에!
        if (delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("already sent");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem:orderItems){
            //this 안 쓴 경우임
            orderItem.cancel();
        }
    }

    //조회 로직
    //전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem: orderItems){
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;

        //위 내용을 좀 간단하게 쓰려면 람다 가능
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
    }
}
