package com.jungook.zerotodeploy.joinMember;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "zerotodeploy", name = "user")
public class JoinUserEntity {
	public JoinUserEntity(String email, String nickname, Object o, Role role) {
	}

	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false, unique = true, name = "user_name")
	private String userName;

	@Column(nullable = false, unique = true, name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "role")
	private Role role;
	public JoinUserEntity(String email, String nickname, Role role) {
		this.email = email;
		this.userName = nickname;
		this.password = null;
		this.role = role;
	}
}
