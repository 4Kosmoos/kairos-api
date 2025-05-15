package dev.kosmoos.kairosapi.security;

import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
    private final UserRepository userRepo;

    public UserSecurity(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean isSelf(Integer userId) {
        String authMail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepo.findById(userId)
                .map(u -> u.getMail().equals(authMail))
                .orElse(false);
    }
}