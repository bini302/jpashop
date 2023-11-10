package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //스프링이랑 테스트
@SpringBootTest //스프링부트 띄우고 테스트하기 (autowired같은거 쓰려면)
@Transactional //테스트에서는 끝나면 알아서 롤백
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    //given, when, then 형식 좋아하신다네

    @Test
    //@Transactional은 기본적으로 롤백함, insert문이 안보이게됨
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId=memberService.join(member);

        //then
        em.flush(); //db에 영속성 컨텍스트를 반영하겠다, 다 하고 다시 롤백함 (insert 확인 가능, 결국 롤백)
        assertEquals(member, memberRepository.findOne(savedId));

        //트랜잭션 안에서, 같은 영속성 컨텍스트 안에서 id가 같으면 같은거임
        //막 2 3개씩 하는게 아니라 딱 하나로 하는거임
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when

//        memberService.join(member1);
//        try {
//            memberService.join(member2); //여기서 예외가 발생해야함
//        }catch (IllegalStateException e) { //이거 예외 종류 맞아야되네
//            return;
//        }

        //expected=IllegalStateException.class를 쓰면 귀찮게 트라이캐치 예외 이런거 안해도댐
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외 발생");
    }
}