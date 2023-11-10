package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        //model에 데이터를 담아 뷰로 넘김
        model.addAttribute("data", "hello!!!");
        //springboot+thymeleaf는 알아서 매핑해줌
        return "hello";
    }
}
