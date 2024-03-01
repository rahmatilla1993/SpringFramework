package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class AuthUserDto {
    private String username;
    private String password;
    private MultipartFile file;
}
