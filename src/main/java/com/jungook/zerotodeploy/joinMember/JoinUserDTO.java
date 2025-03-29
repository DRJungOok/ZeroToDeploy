package com.jungook.zerotodeploy.joinMember;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserDTO {
	private String userId;
	private String email;
	private String password;
}
