package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_permission")
public class Permission extends Auditable {

    private String name;
    private String code;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    @Builder
    public Permission(int id, LocalDateTime createdAt, String name, String code, List<Role> roles) {
        super(id, createdAt);
        this.name = name;
        this.code = code;
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
        }
        this.roles.add(role);
    }
}
