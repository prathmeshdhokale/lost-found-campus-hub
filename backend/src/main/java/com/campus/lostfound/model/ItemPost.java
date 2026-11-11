package com.campus.lostfound.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="item_posts")
public class ItemPost {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String title;
 @Column(nullable=false,length=3000) private String description;
 @Column(nullable=false,length=60) private String category;
 @Column(nullable=false,length=160) private String location;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private ItemType type;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ItemStatus status=ItemStatus.OPEN;
 @Column(length=700) private String imageUrl;
 @Column(nullable=false) private boolean approved=true;
 @Column(nullable=false) private Instant eventDate;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 @ManyToOne(fetch=FetchType.LAZY,optional=false) private AppUser owner;
 protected ItemPost() {}
 public ItemPost(String title,String description,String category,String location,ItemType type,Instant eventDate,AppUser owner){this.title=title;this.description=description;this.category=category;this.location=location;this.type=type;this.eventDate=eventDate;this.owner=owner;}
 public Long getId(){return id;} public String getTitle(){return title;} public String getDescription(){return description;} public String getCategory(){return category;} public String getLocation(){return location;} public ItemType getType(){return type;} public ItemStatus getStatus(){return status;} public String getImageUrl(){return imageUrl;} public boolean isApproved(){return approved;} public Instant getEventDate(){return eventDate;} public Instant getCreatedAt(){return createdAt;} public AppUser getOwner(){return owner;}
 public void update(String title,String description,String category,String location,ItemType type,Instant eventDate){this.title=title;this.description=description;this.category=category;this.location=location;this.type=type;this.eventDate=eventDate;}
 public void setImageUrl(String v){imageUrl=v;} public void setApproved(boolean v){approved=v;} public void setStatus(ItemStatus v){status=v;}
}
