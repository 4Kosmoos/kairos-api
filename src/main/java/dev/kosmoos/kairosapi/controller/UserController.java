package dev.kosmoos.kairosapi.controller;

import dev.kosmoos.kairosapi.dto.UserDTO;
import dev.kosmoos.kairosapi.entity.Event;
import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.Role;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<UserDTO> all() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO one(@PathVariable Integer id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        return toDto(u);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO  create(@RequestBody User dto) {
        if (repo.findByMail(dto.getMail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getRole() == null) {
            dto.setRole(Role.USER);
        }
        User saved = repo.save(dto);
        return toDto(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#id)")
    public UserDTO update(@PathVariable Integer id,
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
        User updated = repo.save(user);
        return toDto(updated);
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

    @GetMapping("/me")
    public UserDTO me(Authentication authentication) {
        String mail = authentication.getName();
        User u = repo.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Utilisateur non trouvé"));
        return toDto(u);
    }

    private UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setMail(u.getMail());
        dto.setRole(u.getRole());
        dto.setIconLink(u.getIconLink());
        dto.setQuote(u.getQuote());
        dto.setColor(u.getColor());
        dto.setEventsCreatedIds(
                u.getEventsCreated().stream()
                        .map(Event::getId)
                        .collect(Collectors.toList())
        );
        dto.setEventsAttendedIds(
                u.getEventsAttended().stream()
                        .map(Event::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}