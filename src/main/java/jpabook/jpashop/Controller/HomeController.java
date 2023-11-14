package jpabook.jpashop.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    //이거 대신 어노테이션 @Slf4j
//    Logger log=LoggerFactory.getLogger(getClass);

    @RequestMapping("/")
    public String home(){
        //어노테이션 쓸 때 이렇게만 해도댐
        log.info("home controller");
        return "home";
    }
}
