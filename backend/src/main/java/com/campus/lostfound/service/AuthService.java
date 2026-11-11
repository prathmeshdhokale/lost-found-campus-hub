package com.campus.lostfound.service;
import com.campus.lostfound.model.*; import com.campus.lostfound.repo.UserRepository; import com.campus.lostfound.security.JwtService; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service;
@Service public class AuthService {
 private final UserRepository users;private final PasswordEncoder encoder;private final JwtService jwt;
 public AuthService(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public record AuthResult(String token,Long id,String name,String email,String role){}
 public AuthResult register(String name,String email,String password){if(users.existsByEmail(email))throw new IllegalArgumentException("Email is already registered");AppUser u=users.save(new AppUser(name,email,encoder.encode(password)));return result(u);}
 public AuthResult login(String email,String password){AppUser u=users.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));if(!encoder.matches(password,u.getPassword()))throw new IllegalArgumentException("Invalid email or password");return result(u);}
 private AuthResult result(AppUser u){return new AuthResult(jwt.create(u.getEmail()),u.getId(),u.getName(),u.getEmail(),u.getRole().name());}
}
