package com.jungook.zerotodeploy.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(ResponseStatusException ex,
                                       Model model,
                                       HttpServletResponse response) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.addAttribute("message", ex.getReason());
            return "error/notFound";
        }
        if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "error/403";
        }
        throw ex;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                     HttpServletResponse response) {
        if (ex.getRequiredType() == Long.class) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "error/notFound";
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "error/badRequest";
    }
}
