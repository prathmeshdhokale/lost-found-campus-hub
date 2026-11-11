package com.campus.lostfound.web;
import com.campus.lostfound.model.ItemPost;import com.campus.lostfound.repo.ItemRepository;import org.springframework.transaction.annotation.Transactional;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/admin") public class AdminController {
 private final ItemRepository items;private final ItemControllerMapper mapper=new ItemControllerMapper();public AdminController(ItemRepository i){items=i;}
 @GetMapping("/items/pending") @Transactional(readOnly=true) public List<ItemController.ItemView> pending(){return items.findByApprovedFalseOrderByCreatedAtDesc().stream().map(mapper::view).toList();}
 @PatchMapping("/items/{id}/review") @Transactional public ItemController.ItemView review(@PathVariable Long id,@RequestParam boolean approved){ItemPost p=items.findById(id).orElseThrow();p.setApproved(approved);return mapper.view(p);}
 static class ItemControllerMapper {ItemController.ItemView view(ItemPost p){return new ItemController.ItemView(p.getId(),p.getTitle(),p.getDescription(),p.getCategory(),p.getLocation(),p.getType(),p.getStatus(),p.getImageUrl(),p.getEventDate(),p.getCreatedAt(),p.getOwner().getId(),p.getOwner().getName(),p.isApproved());}}
}
