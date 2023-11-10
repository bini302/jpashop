package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

//jpa 내장 타입
//@Embeddable, @Embedded 둘 중 하나만 써도 되는데 보통 둘 다 씀
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //생성자, jpa 스펙상 있어야됨(public/protected),,
    protected Address(){}

    //값 타입은 변경 불가능하게 설계 (setter 제거)
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
