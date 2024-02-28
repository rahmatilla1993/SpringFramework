package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "auth_user")
public class AuthUser extends Auditable {

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public AuthUser(int id, LocalDateTime createdAt, String username, String password, Role role) {
        super(id, createdAt);
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
