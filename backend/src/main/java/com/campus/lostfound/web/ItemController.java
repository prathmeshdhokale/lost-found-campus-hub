package com.campus.lostfound.web;

import com.campus.lostfound.model.*;
import com.campus.lostfound.repo.*;
import com.campus.lostfound.service.CloudinaryService;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository items;
    private final UserRepository users;
    private final ClaimRepository claims;
    private final CloudinaryService cloud;

    public ItemController(
            ItemRepository items,
            UserRepository users,
            ClaimRepository claims,
            CloudinaryService cloud
    ) {
        this.items = items;
        this.users = users;
        this.claims = claims;
        this.cloud = cloud;
    }

    public record ItemInput(
            @NotBlank @Size(max = 120) String title,
            @NotBlank @Size(max = 3000) String description,
            @NotBlank String category,
            @NotBlank String location,
            @NotNull ItemType type,
            @NotNull Instant eventDate
    ) {}

    public record ItemView(
            Long id,
            String title,
            String description,
            String category,
            String location,
            ItemType type,
            ItemStatus status,
            String imageUrl,
            Instant eventDate,
            Instant createdAt,
            Long ownerId,
            String ownerName,
            boolean approved
    ) {}

    private ItemView view(ItemPost p) {
        return new ItemView(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getCategory(),
                p.getLocation(),
                p.getType(),
                p.getStatus(),
                p.getImageUrl(),
                p.getEventDate(),
                p.getCreatedAt(),
                p.getOwner().getId(),
                p.getOwner().getName(),
                p.isApproved()
        );
    }

    @Transactional(readOnly = true)
    @GetMapping
    public Page<ItemView> list(
            @RequestParam(required = false) ItemType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page
    ) {
        Specification<ItemPost> s =
                (root, query, cb) -> cb.isTrue(root.get("approved"));

        if (type != null) {
            s = s.and((root, query, cb) ->
                    cb.equal(root.get("type"), type));
        }

        if (category != null && !category.isBlank()) {
            s = s.and((root, query, cb) ->
                    cb.equal(
                            cb.lower(root.get("category")),
                            category.toLowerCase()
                    ));
        }

        if (q != null && !q.isBlank()) {
            String search = "%" + q.toLowerCase() + "%";
            s = s.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("title")), search),
                            cb.like(cb.lower(root.get("description")), search),
                            cb.like(cb.lower(root.get("location")), search)
                    ));
        }

        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                12,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return items.findAll(s, pageable).map(this::view);
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ItemView get(@PathVariable Long id) {
        ItemPost p = items.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        return view(p);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ItemView create(
            @RequestPart("data") ItemInput in,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication a
    ) {
        AppUser u = users.findByEmail(a.getName()).orElseThrow();

        ItemPost p = new ItemPost(
                in.title(),
                in.description(),
                in.category(),
                in.location(),
                in.type(),
                in.eventDate(),
                u
        );

        p.setImageUrl(cloud.upload(image));
        return view(items.save(p));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ItemView update(
            @PathVariable Long id,
            @RequestPart("data") ItemInput in,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication a
    ) {
        ItemPost p = items.findById(id).orElseThrow();

        if (!p.getOwner().getEmail().equals(a.getName())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Not your post"
            );
        }

        p.update(
                in.title(),
                in.description(),
                in.category(),
                in.location(),
                in.type(),
                in.eventDate()
        );

        if (image != null && !image.isEmpty()) {
            p.setImageUrl(cloud.upload(image));
        }

        return view(p);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id, Authentication a) {
        ItemPost p = items.findById(id).orElseThrow();

        if (!p.getOwner().getEmail().equals(a.getName())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Not your post"
            );
        }

        items.delete(p);
    }

    @PostMapping("/{id}/claims")
    @Transactional
    public ClaimController.ClaimView claim(
            @PathVariable Long id,
            @RequestBody ClaimController.ClaimInput in,
            Authentication a
    ) {
        return ClaimController.createClaim(id, in, a, items, users, claims);
    }
}