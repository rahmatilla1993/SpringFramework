package org.example.contoller;

import org.example.dao.AuthUserDao;
import org.example.dto.UserStatusDto;
import org.example.enums.RoleName;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AuthUserDao authUserDao;

    public AdminController(AuthUserDao authUserDao) {
        this.authUserDao = authUserDao;
    }

    @GetMapping("/user_manage")
    public ModelAndView getUserManage() {
        List<UserStatusDto> userList = authUserDao.findAllUsersByRole2(RoleName.ROLE_USER)
                .stream()
                .map(UserStatusDto::new)
                .toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", userList);
        modelAndView.setViewName("admin/user_manage");
        return modelAndView;
    }

    @GetMapping("/user_block/{id}")
    public ModelAndView getUserStatusView(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user_block");
        authUserDao.findById(id)
                .ifPresent(user -> {
                    var userDto = new UserStatusDto(user);
                    modelAndView.addObject("user", userDto);
                });
        return modelAndView;
    }

    @PostMapping("/user_block/{id}")
    public String setUserStatus(@PathVariable("id") int id, @ModelAttribute UserStatusDto dto) {
        dto.setId(id);
        authUserDao.setUserStatus(dto);
        return "redirect:/admin/user_manage";
    }
}
