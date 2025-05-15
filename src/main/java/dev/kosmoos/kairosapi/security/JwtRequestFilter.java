package dev.kosmoos.kairosapi.security;

import dev.kosmoos.kairosapi.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final UserService userService;

    public JwtRequestFilter(JwtUtil jwtUtil,
                            UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        final String authHeader = req.getHeader("Authorization");
        String jwt = null, username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (JwtException ignored) {}
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails ud = userService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, ud)) {
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(
                                ud, null, ud.getAuthorities()
                        );
                token.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        chain.doFilter(req, res);
    }
}
