package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.ItemRepository;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //위에서 readOnly했으니까 오버라이딩
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //준영속성 컨텍스트
    //변경감지 기능 사용
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        //파라미터가 많아서 싫다면 name, price, stockQuantity를 가진 updateItems DTO 만들자

        //준영속성 컨텍스트 1 변경 감지 사용하기
        //새로운 객체를 만들어서 어쩌구가 아니라, 이미 존재하는 영속성 컨텍스트를 가져옴
        Item findItem = itemRepository.findOne(itemId);

        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        findItem.setName(name);
        // ...

        //이렇게 그냥 change를 만들어서 쓰는게 베스트
//        findItem.change(name, price, stockQuantity);

        //@Transactional이 커밋하면 flush를 날림
        //flush: 영속성 컨텍스트에서 변경된 부분을 찾아서 업데이트 쿼리 날림

        //물론 set은 비추. change같은 의미있는 메서드를 쓰자
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
