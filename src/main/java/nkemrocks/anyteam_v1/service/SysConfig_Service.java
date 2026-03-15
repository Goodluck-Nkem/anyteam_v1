package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.entity.Skill_Entity;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.mapper.SysConfig_Mapper;
import nkemrocks.anyteam_v1.repository.Session_Repository;
import nkemrocks.anyteam_v1.repository.Skill_Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static nkemrocks.anyteam_v1.util.GlobalUtil.*;

@Service
@RequiredArgsConstructor
public class SysConfig_Service {
    private final SysConfig_Mapper sysConfigMapper;
    private final Session_Repository sessionRepository;
    private final Skill_Repository skillRepository;

    public static final String[] initialSkillsArray = {
            ART,
            BIOLOGY,
            HISTORY,
            LANGUAGE,
            LOGIC,
            MATH,
            MUSIC,
            SPELLING,
            SPORT,
            TECHNOLOGY,
    };


    @Transactional
    public SysConfig_ResponseDTO initConfigSession(SysConfig_RequestDTO data) {

        /* STEPS:
         * 1.   Check if the <Creation> session has been initialized
         * 2a.  If No, create and save the creation session
         * 2b.  Populate skills
         * 3.   If Yes, just update session's ttl
         * 4.   Return response
         */

        /* 1. Check if the <Creation> session has been initialized */
        Session_Entity configSession = sessionRepository.findBySessionName(GlobalUtil.configSessionName)
                .orElse(null);


        if (configSession == null) {
            /* 2a. If No, create and save the creation session */
            configSession = sessionRepository.save(
                    new Session_Entity(
                            GlobalUtil.configSessionName,
                            data.ttl()
                    ));

            /* 2b.  Populate skills */
            List<Skill_Entity> skills = new ArrayList<>();
            for (String skillName : initialSkillsArray) {
                skills.add(new Skill_Entity(skillName));
            }
            skillRepository.saveAll(skills);
        } else {
            /* 3.  If Yes, just update session's ttl */
            configSession.setTtl(data.ttl());
            sessionRepository.save(configSession);
        }

        /* 4.   Return response */
        return sysConfigMapper.toResponseDTO(
                configSession,
                skillRepository.fetchAllSkillNames()
        );
    }

    public SysConfig_ResponseDTO getInfo() {

        /* STEPS:
         * 1.   Check if the <Creation> session has been initialized
         * 2.   If No, throw exception
         * 3.   If Yes, return response
         */

        /* 1,2. Check if the <Creation> session has been initialized, If No, throw exception */
        Session_Entity configSession = sessionRepository.findBySessionName(GlobalUtil.configSessionName)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) session is not yet initialized!"));


        /* 3.   If Yes, return response */
        return sysConfigMapper.toResponseDTO(
                configSession,
                skillRepository.fetchAllSkillNames()
        );
    }

}
