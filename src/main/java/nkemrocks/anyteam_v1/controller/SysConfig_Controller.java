package nkemrocks.anyteam_v1.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nkemrocks.anyteam_v1.dto.sysConfig.SysConfig_DTO;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import nkemrocks.anyteam_v1.mapper.SysConfig_Mapper;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/sysConfig")
public class SysConfig_Controller {
    private final SysConfig_Service sysConfigService;
    private final SysConfig_Mapper sysConfigMapper;

    @PostMapping
    public ResponseEntity<SysConfig_DTO> setup(@Valid @RequestBody SysConfig_DTO data) {
        SysConfig_Entity sysConfig = sysConfigService.setupCreationSession(data);
        return new ResponseEntity<>(sysConfigMapper.toResponseDTO(sysConfig), HttpStatus.OK);
    }
}
