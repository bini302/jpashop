package jpabook.jpashop.domain.item;

import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
// 전략 선택, 우린 싱글테이블로
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int stockQuantity;

    private int price;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories =new ArrayList<>();

    //비즈니스 로직
    //stockQuantity는 이 안에 있는거, 데이터를 다루고있는 쪽에서 바꾸는게 좀 더 응집도 있는거임
    //setter를 쓰는게 아니라 이 안에서 으쌰으쌰

    //재고 증가
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    //재고 감소
    public void removeStock(int quantity){
        int resStock=this.stockQuantity - quantity;
        if (resStock < 0){
            throw new NotEnoughStockException("nomore stock");
        }
        this.stockQuantity=resStock;
    }
}
