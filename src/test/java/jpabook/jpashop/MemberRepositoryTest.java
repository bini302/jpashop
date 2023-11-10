package jpabook.jpashop;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    //엔티티매니저는 항상 트랜잭션 안에서 작동해야함
    //transactional 어노테이션은 두개인데 스프링 안에서 하는거니까 스프링 ㄱㄱ
    @Transactional
    //test에 이게 있으면 실행한 다음 롤백함
    @Rollback(false)
//    @Rollback(false) 이게 있으면 롤백안함
    public void testMember() throws Exception {
        //given
        Member member=new Member();
        member.setName("memberA");

        //when
        Long saveId=memberRepository.save(member);
        Member findMember=memberRepository.find(saveId);

        //then(검증)
        //검증은 assertionj 라이브러리 사용(스프링 제공)
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        //한 트랜잭션안에서, 영속성 컨텍스트 안에서 식별자가 같으면 같은 애들임
        Assertions.assertThat(findMember).isEqualTo(member); //true
    }
}