package jpabook.jpashop.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

//component 스캔, 빈 등록
@Repository
@RequiredArgsConstructor
public class MemberRepository {
//    @PersistenceContext //JPA가 제공하는 표준 어노테이션
    //근데 스프링에서 autowired도 되게 지원해줌

    //스프링이 생성한 엔티티 매니저를 주입(@requiredArgsConstructor-@autowired-생성자 인젝션)
    private final EntityManager em;
//    public MemberRepository(EntityManager em){    //@RequiredArgsConstructor로 생략된 생성자
//        this.em = em;
//    }

    //직접 주입
//    @PersistenceUnit
//    private EntityManagerFactory ef;

    //영속성 컨텍스트가 됨
    public void save(Member member){
        //persist는 보통 insert까지 안나감
        //db 트랜잭션이 커밋될 때 insert함
        em.persist(member);
    }

    public Member findOne(Long id){
        //(타입, pk)
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //jpql (쿼리, 반환타입)
        //sql은 테이블 대상으로, jpql은 객체(엔티티) 대상으로
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        //jpql
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
