package org.example.contoller;

import org.example.dao.AuthUserDao;
import org.example.dto.AuthUserDto;
import org.example.entity.AuthUser;
import org.example.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserDao dao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthUserDao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
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
        dao.save(AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build());
        return "redirect:/auth/login";
    }
}
