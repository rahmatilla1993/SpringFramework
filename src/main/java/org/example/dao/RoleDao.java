package org.example.dao;

import org.example.entity.Role;
import org.example.enums.RoleName;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RoleDao extends BaseDao<Role, Integer> {

    @Transactional(readOnly = true)
    public Optional<Role> findByCode(RoleName roleName) {
        try {
            Role role = (Role) em.createNativeQuery(
                            "select * from auth_role where cast(code as varchar) = :roleName", Role.class)
                    .setParameter("roleName", roleName.name())
                    .getSingleResult();
            return Optional.of(role);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
