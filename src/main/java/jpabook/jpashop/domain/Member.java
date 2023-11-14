package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
//    @NotEmpty //javax.validation
    private String name;

    //엔티티가 쓰이는 곳이 많은 만큼 수정되는 경우도 있다
    //엔티티를 컨트롤러에서 그대로 쓰면 수정했을때 큰일남
    //그래서 외부로 꺼내고 여기저기에 쓸 DTO를 따로 만듬

    @Embedded
    private Address address;

    //mappedBy: 내가 주인이 아님
    @OneToMany(mappedBy = "member")
    private List<Order> orders=new ArrayList<>();
}
