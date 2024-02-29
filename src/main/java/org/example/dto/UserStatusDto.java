package org.example.dto;

import lombok.*;
import org.example.entity.AuthUser;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserStatusDto {
    private int id;
    private String username;
    private String status;

    public UserStatusDto(AuthUser authUser) {
        this.id = authUser.getId();
        this.username = authUser.getUsername();
        this.status = authUser.getStatus().name();
    }
}
