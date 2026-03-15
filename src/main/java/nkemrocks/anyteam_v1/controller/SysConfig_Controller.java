package nkemrocks.anyteam_v1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sysconfig")
public class SysConfig_Controller {
    private final SysConfig_Service sysConfigService;

    @PostMapping
    public ResponseEntity<SysConfig_ResponseDTO> init(@Valid @RequestBody SysConfig_RequestDTO data) {
        SysConfig_ResponseDTO response = sysConfigService.initConfigSession(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SysConfig_ResponseDTO> info() {
        SysConfig_ResponseDTO response = sysConfigService.getInfo();
        return ResponseEntity.ok(response);
    }
}
