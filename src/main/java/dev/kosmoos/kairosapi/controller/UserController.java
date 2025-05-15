package dev.kosmoos.kairosapi.controller;

import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping
    public List<User> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}