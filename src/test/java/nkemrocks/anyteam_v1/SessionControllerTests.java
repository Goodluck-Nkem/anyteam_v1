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
public class SessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

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
                        .cookie(csrfCookie)
                        .cookie(jwtCookie)
                        .header("X-XSRF-TOKEN", csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(printResponseField("$.sessionId"))
                .andDo(printResponseField("$.dateCreated"))
                .andExpect(status().isCreated());
    }
}
