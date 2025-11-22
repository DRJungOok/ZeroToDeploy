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
	private String userName;
	private String nickname;
	private String email;
	private String password;
}
