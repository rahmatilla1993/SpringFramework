package org.example.contoller;

import org.example.dao.AuthUserDao;
import org.example.dao.RoleDao;
import org.example.dto.AuthUserDto;
import org.example.entity.AuthUser;
import org.example.enums.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserDao authUserDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthUserDao authUserDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.authUserDao = authUserDao;
        this.roleDao = roleDao;
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
    public String registerView() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AuthUserDto dto) {
        roleDao.findByCode(RoleName.ROLE_USER)
                .ifPresent(role -> authUserDao.save(AuthUser.builder()
                        .username(dto.username())
                        .roles(List.of(role))
                        .password(passwordEncoder.encode(dto.password()))
                        .createdAt(LocalDateTime.now())
                        .build())
                );
        return "redirect:/auth/login";
    }
}
