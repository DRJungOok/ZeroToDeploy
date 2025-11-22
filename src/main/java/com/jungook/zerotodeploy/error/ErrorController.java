package com.jungook.zerotodeploy.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            switch (statusCode) {
                case 400:
                    model.addAttribute("errorCode", "400");
                    model.addAttribute("errorTitle", "잘못된 요청");
                    model.addAttribute("errorMessage", "요청이 올바르지 않습니다.");
                    return "error/400";
                case 401:
                    model.addAttribute("errorCode", "401");
                    model.addAttribute("errorTitle", "인증이 필요합니다");
                    model.addAttribute("errorMessage", "이 페이지에 접근하려면 로그인이 필요합니다.");
                    return "error/401";
                case 403:
                    model.addAttribute("errorCode", "403");
                    model.addAttribute("errorTitle", "접근 권한이 없습니다");
                    model.addAttribute("errorMessage", "요청하신 페이지에 접근할 권한이 없습니다.");
                    return "error/403";
                case 404:
                    model.addAttribute("errorCode", "404");
                    model.addAttribute("errorTitle", "페이지를 찾을 수 없습니다");
                    model.addAttribute("errorMessage", "요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.");
                    return "error/404";
                case 500:
                    model.addAttribute("errorCode", "500");
                    model.addAttribute("errorTitle", "서버 내부 오류");
                    model.addAttribute("errorMessage", "서버에서 예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                    return "error/500";
                default:
                    model.addAttribute("errorCode", statusCode);
                    model.addAttribute("errorTitle", "오류가 발생했습니다");
                    model.addAttribute("errorMessage", message != null ? message.toString() : "알 수 없는 오류가 발생했습니다.");
                    return "error/general";
            }
        }
        
        // 상태 코드가 없는 경우
        model.addAttribute("errorCode", "Unknown");
        model.addAttribute("errorTitle", "오류가 발생했습니다");
        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error/general";
    }
}
