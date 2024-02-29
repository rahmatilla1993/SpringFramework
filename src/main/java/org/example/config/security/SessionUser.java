package org.example.config.security;

import org.example.entity.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionUser {

    public Optional<AuthUser> getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return Optional.of(securityUser.authUser());
        }
        return Optional.empty();
    }
}
