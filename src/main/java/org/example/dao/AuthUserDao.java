package org.example.dao;

import jakarta.persistence.ParameterMode;
import org.example.dto.AuthUserDto;
import org.example.dto.UserStatusDto;
import org.example.entity.AuthUser;
import org.example.enums.RoleName;
import org.example.enums.Status;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class AuthUserDao extends BaseDao<AuthUser, Integer> {

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<AuthUser> findAllUsersByRole(RoleName roleName) {
        return em.createStoredProcedureQuery("getusersbyrole", AuthUser.class)
                .registerStoredProcedureParameter("role_name", String.class, ParameterMode.IN)
                .setParameter("role_name", roleName.name())
                .getResultList();
    }

    @Transactional
    public void setUserStatus(UserStatusDto dto) {
        em.createQuery("update AuthUser set status = :status where id = :id")
                .setParameter("status", Status.valueOf(dto.getStatus()))
                .setParameter("id", dto.getId())
                .executeUpdate();
    }

    @Transactional
    public AuthUser saveAuthUser(AuthUserDto dto) {
        return (AuthUser) em.createNativeQuery(
                        "insert into auth_user(username, password) VALUES (:username, :password) returning *;", AuthUser.class)
                .setParameter("username", dto.getUsername())
                .setParameter("password", dto.getPassword())
                .getSingleResult();
    }
}
