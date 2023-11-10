package jpabook.jpashop.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;
    //@RequiredArgsConstructor가 final 보고 알아서 생성자~

    public void save(Item item){
        //id가 없으면 새로 저장함(새 객체)
        if (item.getId() == null){
            em.persist(item);
        } else {
            //merge가 대충 업데이트라고 생각하셈
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    //여러개 찾을땐 jpql
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
