package dev.kosmoos.kairosapi.controller;

import dev.kosmoos.kairosapi.dto.UserAvailabilityDTO;
import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.entity.UserAvailability;
import dev.kosmoos.kairosapi.repository.UserAvailabilityRepository;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/availabilities")
public class UserAvailabilityController {

    private final UserAvailabilityRepository availRepo;
    private final UserRepository userRepo;

    public UserAvailabilityController(UserAvailabilityRepository availRepo,
                                      UserRepository userRepo) {
        this.availRepo = availRepo;
        this.userRepo = userRepo;
    }

    private UserAvailabilityDTO toDto(UserAvailability ua) {
        UserAvailabilityDTO dto = new UserAvailabilityDTO();
        dto.setId(ua.getId());
        dto.setUserId(ua.getUser().getId());
        dto.setStartAt(ua.getStartAt());
        dto.setEndAt(ua.getEndAt());
        dto.setComment(ua.getComment());
        return dto;
    }

    @GetMapping
    public List<UserAvailabilityDTO> list() {
        return availRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserAvailabilityDTO getOne(@PathVariable Integer id) {
        UserAvailability ua = availRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disponibilité non trouvée"));
        return toDto(ua);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserAvailabilityDTO create(@RequestBody Map<String, Object> payload,
                                      Authentication authentication) {
        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepo.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        UserAvailability ua = new UserAvailability();
        ua.setUser(user);

        ua.setStartAt(OffsetDateTime.parse((String) payload.get("startAt")));
        ua.setEndAt(OffsetDateTime.parse((String) payload.get("endAt")));
        ua.setComment((String) payload.getOrDefault("comment", ""));

        UserAvailability saved = availRepo.save(ua);
        return toDto(saved);
    }

    @PutMapping("/{id}")
    public UserAvailabilityDTO update(@PathVariable Integer id,
                                      @RequestBody Map<String, Object> payload,
                                      Authentication authentication) {
        UserAvailability ua = availRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disponibilité non trouvée"));

        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (!ua.getUser().getMail().equals(mail)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Accès refusé");
        }

        if (payload.containsKey("startAt")) {
            ua.setStartAt(OffsetDateTime.parse((String) payload.get("startAt")));
        }
        if (payload.containsKey("endAt")) {
            ua.setEndAt(OffsetDateTime.parse((String) payload.get("endAt")));
        }
        if (payload.containsKey("comment")) {
            ua.setComment((String) payload.get("comment"));
        }

        UserAvailability updated = availRepo.save(ua);
        return toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       Authentication authentication) {
        UserAvailability ua = availRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disponibilité non trouvée"));

        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (!ua.getUser().getMail().equals(mail)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Accès refusé");
        }
        availRepo.deleteById(id);
    }
}
