package com.jungook.zerotodeploy.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode instanceof Integer status) {
            if (status == HttpStatus.NOT_FOUND.value()) {
                return "error/notFound";
            }
            if (status == HttpStatus.BAD_REQUEST.value()) {
                return "error/badRequest";
            }
            if (status == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            }
        }
        return "error/notFound";
    }
}
