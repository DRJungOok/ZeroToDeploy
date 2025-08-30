package com.jungook.zerotodeploy.config;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserModelAdvice {

    private final JoinUserRepo joinUserRepo;

   public GlobalUserModelAdvice(JoinUserRepo joinUserRepo) {
       this.joinUserRepo = joinUserRepo;
   }

   @ModelAttribute("user")
    public JoinUserEntity user(Authentication auth) {
       if(auth == null || !auth.isAuthenticated()) {
           return null;
       }

       String loginId = auth.getName();

       return joinUserRepo.findByUserName(loginId).or(() -> joinUserRepo.findByEmail(loginId)).orElse(null);
   }

   @ModelAttribute("currentUserName")
    public String currentUserName(@ModelAttribute("user") JoinUserEntity user) {
       return (user != null) ? user.getUserName() : null;
   }
}
