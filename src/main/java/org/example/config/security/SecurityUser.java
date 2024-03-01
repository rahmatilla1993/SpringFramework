package org.example.config.security;

import org.example.entity.AuthUser;
import org.example.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record SecurityUser(AuthUser authUser) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authUser.getRoles().forEach(role -> {
            authorityList.add(new SimpleGrantedAuthority(role.getCode().name()));
            role.getPermissions().forEach(permission ->
                    authorityList.add(new SimpleGrantedAuthority(permission.getCode()))
            );
        });
        return authorityList;
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authUser.getStatus().equals(Status.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return authUser.getStatus().equals(Status.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authUser.getStatus().equals(Status.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return authUser.getStatus().equals(Status.ACTIVE);
    }
}
