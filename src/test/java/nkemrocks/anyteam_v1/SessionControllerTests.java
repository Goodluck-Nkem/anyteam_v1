package nkemrocks.anyteam_v1;

import jakarta.transaction.Transactional;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static nkemrocks.anyteam_v1.TestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SysConfig_Service sysConfigService;

    @BeforeEach
    void initTestEnv(){
        sysConfigService.initConfigSession(
                new SysConfig_RequestDTO(
                        3600
                ));
    }

    @Test
    void testThat_validSessionCreateSucceeds() throws Exception{
        printCurrentTestMethodRef();

        String json = """
                {
                    "ttl": 3600,
                    "sessionName": "Fury",
                    "requirements": [ "art", "math", "logic", "math", "history" ]
                }
                """;

        mockMvc.perform(post("/api/v1/session/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(printResponseField("$.sessionId"))
                .andDo(printResponseField("$.dateCreated"))
                .andExpect(status().isCreated());
    }
}
