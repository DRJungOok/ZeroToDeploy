package com.jungook.zerotodeploy.details;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final JoinUserEntity user;

    public CustomUserDetails(JoinUserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> user.getRole().name());
    }

    @Override public String getPassword() { return user.getPassword(); }

    @Override public String getUsername() { return user.getUserName(); }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return true; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return true; }

    public JoinUserEntity getUserEntity() { return user; }
}
