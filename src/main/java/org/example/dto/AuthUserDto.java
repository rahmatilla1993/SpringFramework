package org.example.dto;

import org.springframework.web.multipart.MultipartFile;

public record AuthUserDto(String username, String password, MultipartFile file) { }
