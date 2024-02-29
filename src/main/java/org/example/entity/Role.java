package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.RoleName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_role")
public class Role extends Auditable {

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleName code;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "authrole_authpermission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "UK_role_permission", columnNames = {"role_id", "permission_id"})}
    )
    List<Permission> permissions;

    @Builder
    public Role(int id, LocalDateTime createdAt, String name, RoleName code, List<Permission> permissions) {
        super(id, createdAt);
        this.name = name;
        this.code = code;
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (Objects.isNull(permissions)) {
            permissions = new ArrayList<>();
        }
        this.permissions.add(permission);
    }
}
