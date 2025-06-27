package com.jungook.zerotodeploy.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(ResponseStatusException ex, Model model) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            model.addAttribute("message", ex.getReason());
            return "error/notFound";
        }
        throw ex;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == Long.class) {
            return "error/notFound";
        }
        return "error/badRequest";
    }
}
