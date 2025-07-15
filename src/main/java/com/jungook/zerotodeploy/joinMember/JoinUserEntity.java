package com.jungook.zerotodeploy.joinMember;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "zerotodeploy", name = "join_user")
public class JoinUserEntity {
	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false, unique = true, name = "userName")
	private String userName;

	@Column(nullable = false, unique = true, name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "role")
	private Role role;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "join_date")
	private LocalDateTime joinDate;

	@PrePersist
	public void setJoinDate() {
		if(this.joinDate == null){
			this.joinDate = LocalDateTime.now();
		}
	}
}
