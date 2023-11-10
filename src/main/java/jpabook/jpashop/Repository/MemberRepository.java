package jpabook.jpashop.Repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {
    //JPA가 제공하는 표준 어노테이션
    @PersistenceContext
    //주입
    private EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //jpql (쿼리, 반환타입)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
