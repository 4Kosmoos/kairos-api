package dev.kosmoos.kairosapi.security;

import dev.kosmoos.kairosapi.entity.Event;
import dev.kosmoos.kairosapi.repository.EventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("eventSecurity")
public class EventSecurity {

    private final EventRepository eventRepo;

    public EventSecurity(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    public boolean isCreator(Integer eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetails)) {
            return false;
        }
        String currentMail = ((UserDetails) auth.getPrincipal()).getUsername();
        return eventRepo.findById(eventId)
                .map(Event::getCreator)
                .map(creator -> creator.getMail().equals(currentMail))
                .orElse(false);
    }
}
