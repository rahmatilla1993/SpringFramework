package org.example.contoller;

import org.example.dao.TodoDao;
import org.example.dto.TodoDto;
import org.example.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoDao todoDao;

    @Autowired
    public TodoController(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @GetMapping("/all")
    public ModelAndView todos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("todo/todos");
        modelAndView.addObject("todos", todoDao.findAll());
        return modelAndView;
    }

    @GetMapping("/add")
    public String getAddTodoView(Model model) {
        model.addAttribute("dto", new Todo());
        return "todo/add_todo";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(@PathVariable("id") int id, Model model) {
        Optional<Todo> optionalTodo = todoDao.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            model.addAttribute("todo", todo);
        }
        return "todo/edit_todo";
    }

    @GetMapping("/delete/{id}")
    public String getDeleteView(@PathVariable("id") int id, Model model) {
        Optional<Todo> optionalTodo = todoDao.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            model.addAttribute("todo", todo);
        }
        return "todo/delete_todo";
    }

    @PostMapping("/add")
    public String saveTodo(@ModelAttribute("dto") TodoDto dto) {
        todoDao.save(Todo
                .builder()
                .title(dto.title())
                .priority(dto.priority())
                .createdAt(LocalDateTime.now())
                .build());
        return "redirect:/todo/all";
    }

    @PutMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("todo") Todo todo) {
        todo.setId(id);
        todoDao.edit(todo);
        return "redirect:/todo/all";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        todoDao.delete(id);
        return "redirect:/todo/all";
    }
}
