package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"roles"})
@Entity
@Table(name = "auth_user")
public class AuthUser extends Auditable {

    @Column(unique = true)
    private String username;
    private String password;

    @Builder
    public AuthUser(int id, LocalDateTime createdAt, String username, String password, List<Role> roles) {
        super(id, createdAt);
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @ManyToMany
    @JoinTable(
            name = "authuser_authrole",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "UK_user_role", columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    public void addRole(Role role) {
        if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
        }
        this.roles.add(role);
    }
}
