package nkemrocks.anyteam_v1;

import jakarta.transaction.Transactional;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SysConfigControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testThatSysConfigWillInitFromValidInput() throws Exception {
        Integer ttl = 3600;
        String json = """
                {
                    "ttl": %d
                }
                """.formatted(ttl);

        mockMvc
                .perform(post("/api/v1/sysconfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ttl").value(ttl))
                .andExpect(jsonPath("$.skills.length()")
                        .value(SysConfig_Service.initialSkillsArray.length));
    }

}
