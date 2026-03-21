package nkemrocks.anyteam_v1.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.login.Login_RequestDTO;
import nkemrocks.anyteam_v1.dto.player.response.Player_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.dto.team.response.Team_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.service.Player_Service;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import nkemrocks.anyteam_v1.service.Team_Service;
import nkemrocks.anyteam_v1.util.CookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class Auth_Controller {

    private final SysConfig_Service sysConfigService;
    private final Player_Service playerService;
    private final Team_Service teamService;

    @PostMapping("/admin/login")
    public ResponseEntity<SysConfig_ResponseDTO> loginAdmin(
            @Valid @RequestBody Login_RequestDTO data,
            HttpServletResponse httpServletResponse) {
        SysConfig_ResponseDTO response = sysConfigService.loginAdmin(data, httpServletResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/player/login")
    public ResponseEntity<Player_Fetch_ResponseDTO> loginPlayer(
            @Valid @RequestBody Login_RequestDTO data,
            HttpServletResponse httpServletResponse
    ) {
        Player_Fetch_ResponseDTO response = playerService.loginPlayer(data, httpServletResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/team/login")
    public ResponseEntity<Team_Fetch_ResponseDTO> loginTeam(
            @Valid @RequestBody Login_RequestDTO data,
            HttpServletResponse httpServletResponse
    ){
        Team_Fetch_ResponseDTO response = teamService.loginTeam(data, httpServletResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public void logoutCommonImpl(HttpServletResponse httpServletResponse){
        CookieUtil.clearCookie(httpServletResponse);
    }
}
