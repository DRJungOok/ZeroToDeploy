package com.jungook.zerotodeploy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.jungook.zerotodeploy.chat.ChatRepo;

@SpringBootTest(classes = ZeroToDeployApplication.class)
@ActiveProfiles("test")
class ZeroToDeployApplicationTests {

    @MockBean
    ChatRepo chatRepo;

	@Test
	void contextLoads() {
	}

}
