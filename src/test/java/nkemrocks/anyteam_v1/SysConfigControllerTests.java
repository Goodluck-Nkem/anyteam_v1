package nkemrocks.anyteam_v1;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

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

    private Cookie csrfCookie;
    private String csrfToken;
    private Cookie jwtCookie;

    @BeforeEach
    void loginAdminForCookiesAndTokens() throws Exception {
        /* first login to get JWT and CSRF tokens */
        String json = """
                {
                    "uniqueName": "%s",
                    "password": "%s"
                }
                """.formatted(GlobalUtil.ADMIN_NAME, GlobalUtil.ADMIN_PASSWD);

        MvcResult result = mockMvc
                .perform(post("/api/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(printResponseField("$.ttl"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("XSRF-TOKEN"))
                .andExpect(cookie().exists("jwt"))
                .andReturn();

        csrfCookie = Objects.requireNonNull(result.getResponse().getCookie("XSRF-TOKEN"));
        csrfToken = csrfCookie.getValue();
        jwtCookie = Objects.requireNonNull(result.getResponse().getCookie("jwt"));
    }

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
                        .cookie(csrfCookie)
                        .cookie(jwtCookie)
                        .header("X-XSRF-TOKEN", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                        .content("""
                                { "ttl": 3600 }"""))
                .andDo(printResponseField("$.error"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/sysconfig")
                        .cookie(csrfCookie)
                        .cookie(jwtCookie)
                        .header("X-XSRF-TOKEN", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andDo(printResponseField("$.error"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/sysconfig")
                        .cookie(csrfCookie)
                        .cookie(jwtCookie)
                        .header("X-XSRF-TOKEN", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andDo(printResponseField("$.error"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testThat_GetInfoSucceeds() throws Exception {
        printCurrentTestMethodRef();

        sysConfigService.initConfigSession(new SysConfig_RequestDTO(3600L));

        mockMvc.perform(get("/api/v1/sysconfig"))
                .andDo(printResponseField("$.ttl"))
                .andExpect(status().isOk());
    }
}
