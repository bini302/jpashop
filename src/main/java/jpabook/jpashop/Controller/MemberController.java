package jpabook.jpashop.Controller;

import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        //model:
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }
    @PostMapping("/members/new")
    //@Valid를 보면 스프링이 memberForm이 validation을 쓴다는걸 알 수 있음
    //@valid 다음에 bindingResult가 있으면 오류가 담긴 상태로 코드가 실행됨
    public String create(@Valid MemberForm form, BindingResult result){
        //빈칸 빠꾸
        if (result.hasErrors()){
            //다시 createMemberForm으로 돌아가서 우리가 쓴 오류가 뜸 ㅋㅋ 간지
            return "members/createMemberForm";
        }

        //이 작업은 MemberForm에서 해도 되겠죠
        Address address=new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member=new Member();
        member.setName(form.getName());
        member.setAddress(address);

        //실무에선 실제 entity를 form으로 쓰기 힘듬 복잡해서
        //그냥 맞는 form을 만들고, 정제한 값을 넘기자

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        //이거도 단순하니까 그런거지 앵간하면 DTO로 변환해서 정말 필요한거만 뿌리자
        //api 만들땐! 절대! entity를! 외부로 반환하지 말자!!!
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }

}
