package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //fk가 어디에 있어도 상관없는데 기왕이면 자주 쓰는데가 편하지않을까요
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) //거울
    private Order order;

    @Embedded
    private Address address;

    // 배송상태, READY/COMP
    // enumType은 string, ordinal( 디폴트, 숫자, 개비추)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
