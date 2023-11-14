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
            em.merge(item);

            //merge가 대충 업데이트라고 생각하셈
            //준영속성 컨텍스트 1 merge 사용 (밝혀지는 merge의 비밀)
            //편하긴하지만 지양하자

            //영속성 컨텍스트에서 (준영속성 컨텍스트 1 변경감지 기능 사용) 파라미터로 모든 데이터를 바꿈
            //즉, 파라미터로 넘어온 준영속성 엔티티의 식별자 값으로 1차 캐시에서 엔티티 조회
            //  > 조회한 엔티티에 파라미터의 모든 값을 넣음
            // 영속성 A로 준영속성 B를 merge 시켜도, B는 여전히 준영속성
//            Item mergeItem=em.merge(item);
            //마지막에 인자로 전달되는 item이 파라미터임
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
