package jpabook.jpashop.Controller;

import jpabook.jpashop.Service.ItemService;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }
    @PostMapping("/items/new")
    public String create(BookForm form){
        Book book=new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());
        //set보다 entity에서 createBook을 만들어 파라미터 전달하는게 더 나은 설계입니다~

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        //이거도 실무에선 entity 반환하지맙시다~
        List<Item> items=itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        //캐스팅이 좋은건 아닌데 일단은~
        Book item = (Book)itemService.findOne(itemId);

        BookForm form=new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setAuthor(item.getAuthor());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    @PostMapping("items/{itemId}/edit")
    //html 보면 넘겨줄 때 form으로 넘겨줘서 @ModelAttribute 쓰기
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){
//        Book book=new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        //실무에서 id 조심해,,
//        //유저의 item에 대한 권한 확인 / 세션 / ,,,
//
//        itemService.saveItem(book);
//        //엔티티가 itemService.saveItem > itemRepository.save > id가 없으면 persist(생성), 있으면 merge

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
