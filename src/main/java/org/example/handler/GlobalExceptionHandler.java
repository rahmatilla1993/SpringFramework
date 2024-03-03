package org.example.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.example.exception.TodoNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice("org.example")
public class GlobalExceptionHandler {
    @ExceptionHandler({TodoNotFoundException.class})
    public ModelAndView handleTodoNotFoundException(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setViewName("error/404");
        return modelAndView;
    }
}
