package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class AuthUserDto {
    @NotBlank(message = "valid.not_blank")
    private String username;

    @NotBlank(message = "valid.not_blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", message = "valid.password")
    private String password;

    private String confirmPassword;

    private MultipartFile file;
}
