package com.jungook.zerotodeploy.joinMember;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false, unique = true, name = "user_id")
	private String userId;

	@Column(nullable = false, unique = true, name = "email")
	private String email;

	@Column(nullable = false, name = "password")
	private String password;

	@Column(nullable = false, name="role")
	private String role;
}
