package com.campus.lostfound.model;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="claim_requests",uniqueConstraints=@UniqueConstraint(columnNames={"item_post_id","claimant_id"}))
public class ClaimRequest {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private ItemPost itemPost;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private AppUser claimant;
 @Column(nullable=false,length=1000) private String message;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ClaimStatus status=ClaimStatus.PENDING;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected ClaimRequest() {}
 public ClaimRequest(ItemPost itemPost,AppUser claimant,String message){this.itemPost=itemPost;this.claimant=claimant;this.message=message;}
 public Long getId(){return id;} public ItemPost getItemPost(){return itemPost;} public AppUser getClaimant(){return claimant;} public String getMessage(){return message;} public ClaimStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public void setStatus(ClaimStatus v){status=v;}
}
