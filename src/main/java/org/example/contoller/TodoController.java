package org.example.contoller;

import org.example.config.security.SecurityUser;
import org.example.dao.AuthUserDao;
import org.example.dao.TodoDao;
import org.example.dto.TodoDto;
import org.example.entity.AuthUser;
import org.example.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoDao todoDao;
    private final AuthUserDao authUserDao;

    @Autowired
    public TodoController(TodoDao todoDao, AuthUserDao authUserDao) {
        this.todoDao = todoDao;
        this.authUserDao = authUserDao;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView todos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todo/todos");
        findUserByUsername().ifPresent(user -> {
                    List<Todo> todoList = todoDao.findAllByCreatedUser(user);
                    modelAndView.addObject("todos", todoList);
                }
        );
        return modelAndView;
    }

    @GetMapping("/add")
    public String getAddTodoView() {
        return "todo/add_todo";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(@PathVariable("id") int id, Model model) {
        todoDao.findById(id)
                .ifPresent(todo -> model.addAttribute("todo", todo));
        return "todo/edit_todo";
    }

    @GetMapping("/delete/{id}")
    public String getDeleteView(@PathVariable("id") int id, Model model) {
        todoDao.findById(id)
                .ifPresent(todo -> model.addAttribute("todo", todo));
        return "todo/delete_todo";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String saveTodo(@ModelAttribute TodoDto dto) {
        findUserByUsername().ifPresent(user -> todoDao.save(Todo
                .builder()
                .title(dto.title())
                .priority(dto.priority())
                .createdAt(LocalDateTime.now())
                .createdUser(user)
                .build())
        );
        return "redirect:/todo/all";
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String edit(@PathVariable("id") int id, @ModelAttribute TodoDto dto) {
        todoDao.findById(id).ifPresent(todo -> {
            todo.setPriority(dto.priority());
            todo.setTitle(dto.title());
            todoDao.edit(todo);
        });
        return "redirect:/todo/all";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String delete(@PathVariable("id") int id) {
        todoDao.delete(id);
        return "redirect:/todo/all";
    }

    private Optional<AuthUser> findUserByUsername() {
        String username = ((SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
        return authUserDao.findByUsername(username);
    }
}
