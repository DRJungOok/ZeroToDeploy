package com.jungook.zerotodeploy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ZeroToDeployApplication.class)
@ActiveProfiles("test")
class ZeroToDeployApplicationTests {

	@Test
	void contextLoads() {
	}

}
