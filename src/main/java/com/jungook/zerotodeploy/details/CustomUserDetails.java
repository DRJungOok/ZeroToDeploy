package com.jungook.zerotodeploy.details;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 세션 직렬화를 위해 필요한 필드만 보관
    private final Long id;
    private final String username;
    private final String password;
    private final String role;
    private final String profileImage; // 프로필 이미지 추가

    // 필요 시 런타임 참조용(세션 직렬화 제외)
    private transient JoinUserEntity user;

    public CustomUserDetails(JoinUserEntity user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.role = user.getRole().name();
        this.profileImage = user.getProfileImage(); // 프로필 이미지 초기화
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
