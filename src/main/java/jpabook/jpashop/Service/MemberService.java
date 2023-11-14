package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//앵간하면 꼭 트랜잭션 안에서 (spring에서 제공하는거, 쓸 수 있는 옵션이 많음)
@Transactional(readOnly = true)
//밑에 생성자 인젝션에서 생성자를 알아서 만들어줌
//@AllArgsConstructor
//final이 있는 필드만 생성자 인젝션의 생성자를 만들어줌(사실상 최종)
@RequiredArgsConstructor
public class MemberService {

    //필드 인젝션
    //이렇게 많이 쓰긴 하지만 바꿀수가 없어서 setter 인젝션을 사용함
//    @Autowired
//    private MemberRepository memberRepository;

    //setter injection
    //테스트할 때 가짜를 사용해서 편할 수 있음
    //근데? 생각해보면? 이미 조립 다 해놓은거? 건들일이 없음
//    private MemberRepository memberRepository;
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //생성자 인젝션
    //테스트케이스를 작성할때 memberservice 생성자에서 목이든 뭐든 주입하면댐
    //final 쓰면 체크해줘서 추천드립니다~
    private final MemberRepository memberRepository;
    //생성자 하나만 있어도 알아서 autowired로 주입해줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository=memberRepository;
//    }

    //회원 가입
    //디폴트가 readOnly=false
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers=memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("already exist");
        }
        //근데 만약에 두 사람이 같은 이름으로 동시에 회원가입 > 둘 다 회원가입이 됨
        //그래서 실무에선 unique처럼 한번더 막아야댐
    }

    //회원 전체 조회
    //조회만할때는 가급적 readOnly=true
//    @Transactional(readOnly = true)
    public List<Member>findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        //transaction이 있는데서 조회하면 영속성 가져오고
        //이름 바꾸고
        //커밋할때 변경감지 > 업데이트
        //트랜잭션 끝
        Member member = memberRepository.findOne(id);
        member.setName(name);

        //void가 아니라 member를 반환해도 되긴하는데,, 으음,,
    }
}
