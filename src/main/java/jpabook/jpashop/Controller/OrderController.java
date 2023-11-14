package jpabook.jpashop.Controller;

import jpabook.jpashop.Repository.OrderSearch;
import jpabook.jpashop.Service.ItemService;
import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.Service.OrderService;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "/order/orderForm";
    }
    @PostMapping("/order")
    //submit 방식일때 @RequestParam
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        orderService.order(memberId, itemId,count);
        //컨트롤러에서 엔티티 찾아서 넘길수도 있지만 더럽고 테스트도 그렇고 좀,, 상관은 없는데,,
        //서비스에서 트랜잭션 안에서 엔티티를 조회하는게 낫다
        //1 서비스가 엔티티에 더 많이 의존한다
        //2 트랜잭션 안에서 돌리는게 깔끔하다
        //여기서 조회해서 넘기면 그건 영속성 컨텍스트가 아니야

        return "redirect:/orders";
    }

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
