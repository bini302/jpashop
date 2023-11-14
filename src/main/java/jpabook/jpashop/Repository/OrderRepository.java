package jpabook.jpashop.Repository;

import org.springframework.util.StringUtils;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    //동적쿼리 처리 1. 이런식으로 경우에 따라 jpql 노가다하기 (비추)
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql="select o From Order o join o.member m";
        boolean isFirstCondition=true;
        //주문상태검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql+=" where";
                isFirstCondition=false;
            } else {
                jpql +=" and";
            }
            jpql+=" m.name like :name";
        }
        TypedQuery<Order> query=em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null){
            query=query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query=query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
        //짱 힘들죠?
    }

//    //동적쿼리 처리 2. jpa criteria 사용해서 jpql 만들기 (비추)
    public List<Order> findAllByCriteria (OrderSearch orderSearch){
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Order> cq=cb.createQuery(Order.class);
        Root<Order> o=cq.from(Order.class);
        Join<Object, Object> m= o.join("member", JoinType.INNER);

        List<Predicate> criteria=new ArrayList<>();

        //주문상태 검색
        if (orderSearch.getOrderStatus()!=null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getMemberName()+"%");
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name=cb.like(m.<String>get("name"), "%"+orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query=em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
//    //직관적이지가 않음 (유지보수가 너무 힘듬)

    //동적쿼리 처리 3. queryDSL (추천!)


    //        return em.createQuery("Select o from Order o join o.member m"
//                +" where o.status=:status"
//                +" and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000) //최대 1000개 조회
//                .getResultList();
}
