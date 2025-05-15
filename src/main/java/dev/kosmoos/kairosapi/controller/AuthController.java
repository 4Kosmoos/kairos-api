package dev.kosmoos.kairosapi.controller;


import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.repository.UserRepository;
import dev.kosmoos.kairosapi.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepo,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User dto) {
        if (userRepo.findByMail(dto.getMail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRole(dto.getRole() == null ?
                dev.kosmoos.kairosapi.Role.USER : dto.getRole());
        User saved = userRepo.save(dto);
        String token = jwtUtil.generateToken(saved.getMail());
        return Map.of("userId", saved.getId(), "token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> cred) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        cred.get("mail"), cred.get("password")
                )
        );
        String token = jwtUtil.generateToken(cred.get("mail"));
        return Map.of("token", token);
    }
}
