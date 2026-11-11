package com.campus.lostfound.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="users")
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=80) private String name;
 @Column(nullable=false,unique=true,length=160) private String email;
 @Column(nullable=false) private String password;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Role role=Role.USER;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected AppUser() {}
 public AppUser(String name,String email,String password){this.name=name;this.email=email;this.password=password;}
 public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPassword(){return password;} public Role getRole(){return role;} public Instant getCreatedAt(){return createdAt;}
}
