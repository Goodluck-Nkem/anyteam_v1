package nkemrocks.anyteam_v1;

import jakarta.transaction.Transactional;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
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
public class SysConfigControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SysConfig_Service sysConfigService;

    @Test
    void testThat_SysConfigWillInitForValidInput() throws Exception {
        printCurrentTestMethodRef();

        Long ttl = 3600L;
        String json = """
                {
                    "ttl": %d
                }
                """.formatted(ttl);

        mockMvc.perform(post("/api/v1/sysconfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                //.andDo(print())
                .andDo(printResponseField("$.ttl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ttl").value(ttl))
                .andExpect(jsonPath("$.skills.length()")
                        .value(SysConfig_Service.initialSkillsArray.length));
    }

    @Test
    void testThat_SysConfigWillAbortForInvalidInput() throws Exception {
        printCurrentTestMethodRef();

        String json1 = """
                {
                    "ttl": null
                }
                """;
        String json2 = "{}";

        mockMvc.perform(post("/api/v1/sysconfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                //.andDo(print())
                .andDo(printResponseField("$.error"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/sysconfig")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                //.andDo(print())
                .andDo(printResponseField("$.error"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testThat_GetInfoWithoutSysConfigAborts() throws Exception {
        printCurrentTestMethodRef();

        mockMvc.perform(get("/api/v1/sysconfig"))
                .andDo(printResponseField("$.error"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void testThat_GetInfoWithValidSysConfigSucceeds() throws Exception {
        printCurrentTestMethodRef();

        sysConfigService.initConfigSession(new SysConfig_RequestDTO(3600L));

        mockMvc.perform(get("/api/v1/sysconfig"))
                .andDo(printResponseField("$.ttl"))
                .andExpect(status().isOk());
    }
}
