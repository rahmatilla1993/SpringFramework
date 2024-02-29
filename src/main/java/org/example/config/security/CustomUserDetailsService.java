package org.example.config.security;

import org.example.dao.AuthUserDao;
import org.example.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserDao authUserDao;

    @Autowired
    public CustomUserDetailsService(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthUser> optionalAuthUser = authUserDao.findByUsername(username);
        if (optionalAuthUser.isPresent()) {
            AuthUser authUser = optionalAuthUser.get();
            return new UserSecurity(authUser);
        } else {
            throw new UsernameNotFoundException("User with '%s' username not found!".formatted(username));
        }
    }
}
