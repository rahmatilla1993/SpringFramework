package org.example.contoller;

import jakarta.validation.Valid;
import org.example.config.security.SessionUser;
import org.example.dao.TodoDao;
import org.example.dto.TodoDto;
import org.example.entity.Todo;
import org.example.exception.TodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoDao todoDao;
    private final SessionUser sessionUser;
    private final MessageSource messageSource;

    @Autowired
    public TodoController(TodoDao todoDao, SessionUser sessionUser, MessageSource messageSource) {
        this.todoDao = todoDao;
        this.sessionUser = sessionUser;
        this.messageSource = messageSource;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority(T(org.example.config.security.Permissions).GET_TODOS)")
    public ModelAndView todos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todo/todos");
        sessionUser.getAuthUser()
                .ifPresent(user -> {
                            List<Todo> todoList = todoDao.findAllByCreatedUser(user);
                            modelAndView.addObject("todos", todoList);
                        }
                );
        return modelAndView;
    }

    @GetMapping("/add")
    public String getAddTodoView(Model model) {
        model.addAttribute("dto", new TodoDto());
        return "todo/add_todo";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(@PathVariable("id") int id, Model model,
                              @CookieValue(name = "language") String lang) {
        helper(id, model, lang);
        return "todo/edit_todo";
    }

    @GetMapping("/delete/{id}")
    public String getDeleteView(@PathVariable("id") int id, Model model,
                                @CookieValue(name = "language") String lang) {
        helper(id, model, lang);
        return "todo/delete_todo";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(T(org.example.config.security.Permissions).ADD_TODO)")
    public String saveTodo(@Valid @ModelAttribute("dto") TodoDto dto,
                           BindingResult errors) {
        if (errors.hasErrors()) {
            return "todo/add_todo";
        }
        sessionUser.getAuthUser()
                .ifPresent(user -> todoDao.save(Todo
                        .builder()
                        .title(dto.getTitle())
                        .priority(dto.getPriority())
                        .createdAt(LocalDateTime.now())
                        .createdUser(user)
                        .build())
                );
        return "redirect:/todo/all";
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAuthority(T(org.example.config.security.Permissions).EDIT_TODO)")
    public String edit(@PathVariable("id") int id,
                       @Valid @ModelAttribute("dto") TodoDto dto, BindingResult errors) {
        if (errors.hasErrors()) {
            return "todo/edit_todo";
        }
        todoDao.findById(id).ifPresent(todo -> {
            todo.setPriority(dto.getPriority());
            todo.setTitle(dto.getTitle());
            todoDao.edit(todo);
        });
        return "redirect:/todo/all";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority(T(org.example.config.security.Permissions).DELETE_TODO)")
    public String delete(@PathVariable("id") int id) {
        todoDao.delete(id);
        return "redirect:/todo/all";
    }

    private void helper(int id, Model model, String lang) {
        todoDao.findById(id)
                .ifPresentOrElse(
                        (todo -> model.addAttribute("dto", new TodoDto(todo))),
                        (() -> {
                            String message = messageSource.getMessage(
                                    "todo_not_found",
                                    new Object[]{id},
                                    Locale.forLanguageTag(lang)
                            );
                            throw new TodoNotFoundException(message);
                        })
                );
    }
}
