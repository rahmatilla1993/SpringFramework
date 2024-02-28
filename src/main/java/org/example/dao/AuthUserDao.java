package org.example.dao;

import org.example.entity.AuthUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUserDao extends BaseDao<AuthUser, Integer> {

    public Optional<AuthUser> findByUsername(String username) {
        try {
            AuthUser authUser = em.createQuery("from AuthUser where username = :username", AuthUser.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(authUser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
