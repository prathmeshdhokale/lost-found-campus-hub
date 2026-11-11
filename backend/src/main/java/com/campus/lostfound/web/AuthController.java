package com.campus.lostfound.web;
import com.campus.lostfound.service.AuthService; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController {
 private final AuthService auth;public AuthController(AuthService a){auth=a;}
 public record Register(@NotBlank @Size(max=80) String name,@Email @NotBlank String email,@Size(min=8,max=72) String password){} public record Login(@Email @NotBlank String email,@NotBlank String password){}
 @PostMapping("/register") public AuthService.AuthResult register(@Valid @RequestBody Register r){return auth.register(r.name(),r.email().toLowerCase(),r.password());}
 @PostMapping("/login") public AuthService.AuthResult login(@Valid @RequestBody Login r){return auth.login(r.email().toLowerCase(),r.password());}
}
