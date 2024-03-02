package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Status;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private FileStorage fileStorage;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "authuser_authrole",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "UK_user_role", columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    @Builder
    public AuthUser(int id, LocalDateTime createdAt, String username, String password, List<Role> roles, Status status) {
        super(id, createdAt);
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.status = status;
    }

    public void addRole(Role role) {
        if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
        }
        this.roles.add(role);
    }
}
