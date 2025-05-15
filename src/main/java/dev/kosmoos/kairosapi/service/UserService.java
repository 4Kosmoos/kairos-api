package dev.kosmoos.kairosapi.service;


import dev.kosmoos.kairosapi.entity.User;
import dev.kosmoos.kairosapi.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = repo.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        return new org.springframework.security.core.userdetails.User(
                user.getMail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
