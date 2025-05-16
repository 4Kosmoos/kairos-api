package dev.kosmoos.kairosapi.controller;

import dev.kosmoos.kairosapi.dto.EventDTO;
import dev.kosmoos.kairosapi.entity.Event;
import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.repository.EventRepository;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;

    public EventController(EventRepository eventRepo,
                           UserRepository userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<EventDTO> list() {
        return eventRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventDTO  getOne(@PathVariable Integer id) {
        Event ev = eventRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement non trouvé"));
        return toDto(ev);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO  create(@RequestBody Map<String, Object> payload,
                        Authentication authentication) {
        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User creator = userRepo.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        Event ev = new Event();

        String startAtStr = (String) payload.get("startAt");
        if (startAtStr == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "startAt manquant");
        }
        ev.setStartAt(OffsetDateTime.parse(startAtStr));

        String endAtStr = (String) payload.get("endAt");
        if (endAtStr == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "endAt manquant");
        }
        ev.setEndAt(OffsetDateTime.parse(endAtStr));

        ev.setTitle((String) payload.getOrDefault("title", ""));
        ev.setDescription((String) payload.getOrDefault("description", null));

        ev.setCreator(creator);

        ev = eventRepo.save(ev);
        return toDto(ev);
    }


    @PutMapping("/{id}")
    @PreAuthorize("@eventSecurity.isCreator(#id)")
    public EventDTO update(@PathVariable Integer id,
                        @RequestBody Map<String, Object> payload) {
        Event ev = eventRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement non trouvé"));

        if (payload.containsKey("startAt")) {
            ev.setStartAt(OffsetDateTime.parse((String) payload.get("startAt")));
        }
        if (payload.containsKey("endAt")) {
            Object e = payload.get("endAt");
            ev.setEndAt(e == null ? null : OffsetDateTime.parse((String) e));
        }
        if (payload.containsKey("title")) {
            ev.setTitle((String) payload.get("title"));
        }
        if (payload.containsKey("description")) {
            ev.setDescription((String) payload.get("description"));
        }
        if (payload.containsKey("creatorId")) {
            Integer creatorId = (Integer) payload.get("creatorId");
            userRepo.findById(creatorId).ifPresent(ev::setCreator);
        }

        ev = eventRepo.save(ev);
        return toDto(ev);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@eventSecurity.isCreator(#id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (!eventRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Événement non trouvé");
        }
        eventRepo.deleteById(id);
    }

    @PostMapping("/{id}/attendees")
    @PreAuthorize("isAuthenticated()")
    public EventDTO joinEvent(@PathVariable Integer id,
                           Authentication authentication) {
        Event ev = eventRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement non trouvé"));

        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepo.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        if (ev.getCreator().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Le créateur ne peut pas s'inscrire à son propre événement"
            );
        }

        if (!ev.getAttendees().contains(user)) {
            ev.addAttendee(user);
            ev = eventRepo.save(ev);
        }
        return toDto(ev);
    }

    @DeleteMapping("/{id}/attendees")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveEvent(@PathVariable Integer id,
                           Authentication authentication) {
        Event ev = eventRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement non trouvé"));

        String mail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepo.findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        if (ev.getCreator().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Le créateur ne peut pas se désinscrire de son propre événement"
            );
        }

        if (ev.getAttendees().contains(user)) {
            ev.removeAttendee(user);
            eventRepo.save(ev);
        }
    }

    private EventDTO toDto(Event ev) {
        EventDTO dto = new EventDTO();
        dto.setId(ev.getId());
        dto.setStartAt(ev.getStartAt());
        dto.setEndAt(ev.getEndAt());
        dto.setTitle(ev.getTitle());
        dto.setDescription(ev.getDescription());
        dto.setCreatorId(ev.getCreator().getId());
        dto.setAttendeeIds(
                ev.getAttendees().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}