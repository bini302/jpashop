package jpabook.jpashop.api;

import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController //@Controller + @ResponseBody
//@ResponseBody: 데이터를 바로 json이나 xml로 보내자
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        //@Valid: Member.name에 @NotEmpty 넣어뒀음
        //@RequestBody: json으로 온 정보를 Member에 알아서 맞게 넣어줌
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    //~~ 엔티티를 절대 그대로 주고받고 쓰지마 외부에 노출하지도 마 ~~

//    @GetMapping("/api/v1/members")
//    public List<Member> membersV1(){
//        return memberService.findMembers();
//        //이거는 원하지 않은 order 정보까지 넘어감
//        //그럼 entity에 @JsonIgnore을 써서 안넘어가게 만들어
//        //근데? 그럼 order가 필요할땐? order 올려 member 내려 order 내리지말고 올려
//    }
//    //~~ 엔티티를 절대 그대로 주고받고 쓰지마 외부에 노출하지도 마 ~~

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        //엔티티 가져와서
        List<Member> findMembers=memberService.findMembers();
        //DTO로 변환
        List<MemberDto> collect=findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        //그냥 json으로 보내는거보다 이게 낫다
        return new Result(collect);
    }



    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    //엔티티가 수정돼도 api 스펙은 바꿀거 없음
    //클래스를 하나 만들어야댐(별도의 DTO)

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberReaponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberReaponse(findMember.getId(), findMember.getName());
    }



    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
    @Data
    static class CreateMemberRequest {
        //name만 받는다 (api 스펙에 맞는 dto를 만든다)
        //엔티티에 NotEmpty를 넣고 사용하면 모든 곳에서 반드시 NotEmpty여야 함
        //이걸 쓰면 각 상황에 맞게 이것저것 꾸리는게 가능
        //이거만 봐도 다 알 수 있으니 유지보수가 훨씬 쉬워짐
        private String name;
    }

    @Data
    @AllArgsConstructor //인자로 id, name 다 전달해야댐
    static class UpdateMemberReaponse {
        private Long id;
        private String name;
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        //실제로 노출시킬것만
        private String name;
    }


}
