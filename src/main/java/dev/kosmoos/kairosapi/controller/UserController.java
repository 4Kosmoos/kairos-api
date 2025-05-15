package dev.kosmoos.kairosapi.controller;

import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.Role;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repo;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User dto) {
        if (repo.findByMail(dto.getMail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getRole() == null) {
            dto.setRole(Role.USER);
        }
        return repo.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public User update(@PathVariable Integer id,
                       @RequestBody Map<String, Object> payload) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        if (payload.containsKey("name")) {
            user.setName((String) payload.get("name"));
        }
        if (payload.containsKey("mail")) {
            user.setMail((String) payload.get("mail"));
        }
        if (payload.containsKey("password")) {
            user.setPassword(passwordEncoder.encode((String) payload.get("password")));
        }
        if (payload.containsKey("iconLink")) {
            user.setIconLink((String) payload.get("iconLink"));
        }
        if (payload.containsKey("quote")) {
            user.setQuote((String) payload.get("quote"));
        }
        if (payload.containsKey("color")) {
            user.setColor((String) payload.get("color"));
        }
        return repo.save(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        repo.deleteById(id);
    }
}