package org.example.contoller;

import jakarta.validation.Valid;
import org.example.dao.AuthUserDao;
import org.example.dao.FileStorageDao;
import org.example.dao.RoleDao;
import org.example.dto.AuthUserDto;
import org.example.entity.AuthUser;
import org.example.entity.FileStorage;
import org.example.entity.Role;
import org.example.enums.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserDao authUserDao;
    private final RoleDao roleDao;
    private final FileStorageDao fileStorageDao;
    private final PasswordEncoder passwordEncoder;
    private final Path rootPath = Path.of(System.getProperty("user.home"), "/download");

    @Autowired
    public AuthController(AuthUserDao authUserDao,
                          RoleDao roleDao,
                          FileStorageDao fileStorageDao,
                          PasswordEncoder passwordEncoder
    ) {
        this.authUserDao = authUserDao;
        this.roleDao = roleDao;
        this.fileStorageDao = fileStorageDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String errorMsg) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/login");
        modelAndView.addObject("errorMsg", errorMsg);
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout() {
        return "auth/logout";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        model.addAttribute("dto", new AuthUserDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("dto") AuthUserDto dto,
                           BindingResult errors, Model model) {
        if (!checkConfirmPassword(dto)) {
            errors.rejectValue("confirmPassword", "conf_password");
        }
        if (errors.hasErrors()) {
            return "auth/register";
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        authUserDao.findByUsername(dto.getUsername())
                .ifPresentOrElse((authUser -> {
                    model.addAttribute("error", authUser.getUsername());
                    errors.rejectValue("username", "user_ex");
                }), (() -> {
                    Role role = roleDao.findByCode(RoleName.ROLE_USER).get();
                    AuthUser authUser = authUserDao.saveAuthUser(dto);
                    authUser.setRoles(List.of(role));
                    authUserDao.edit(authUser);
                    uploadFile(dto.getFile(), authUser);
                }));

        // agar username database da bor bo'lsa
        if (errors.hasErrors()) {
            return "auth/register";
        }
        return "redirect:/auth/login";
    }

    private void uploadFile(MultipartFile file, AuthUser user) {
        String originalName = file.getOriginalFilename();
        String generatedName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalName);
        FileStorage storage = FileStorage.builder()
                .originalName(originalName)
                .generatedName(generatedName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        fileStorageDao.save(storage);
        try {
            Files.copy(file.getInputStream(), rootPath.resolve(generatedName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkConfirmPassword(AuthUserDto dto) {
        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
