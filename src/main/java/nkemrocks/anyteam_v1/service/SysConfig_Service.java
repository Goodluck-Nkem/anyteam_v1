package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.dto.sysConfig.response.SysConfig_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.entity.Skill_Entity;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.mapper.SysConfig_Mapper;
import nkemrocks.anyteam_v1.repository.Session_Repository;
import nkemrocks.anyteam_v1.repository.Skill_Repository;
import nkemrocks.anyteam_v1.repository.SysConfig_Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static nkemrocks.anyteam_v1.util.GlobalUtil.*;

@Service
@RequiredArgsConstructor
public class SysConfig_Service {
    private final SysConfig_Mapper sysConfigMapper;
    private final SysConfig_Repository sysConfigRepository;
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
    public SysConfig_ResponseDTO initCreationSession(SysConfig_RequestDTO data) {

        /* STEPS:
         * 1.   Check if the <Creation> session has been initialized
         * 2a.  If No, create the creation session
         * 2b.  Populate skills
         * 2c.  Then save sysConfig only record
         * 3.   If Yes, just update session's ttl
         * 4.   Return response
         */

        /* 1. Check if the <Creation> session has been initialized */
        SysConfig_Entity sysConfig = sysConfigRepository.findById(1L)
                .orElse(null);

        if (sysConfig == null) {
            /* 2a. If No, create the creation session */
            Session_Entity creationSession = new Session_Entity(
                    GlobalUtil.creationSessionName,
                    data.ttl()
            );

            /* 2b.  Populate skills */
            List<Skill_Entity> skills = new ArrayList<>();
            for (String skillName : initialSkillsArray) {
                skills.add(new Skill_Entity(skillName));
            }
            skillRepository.saveAll(skills);

            /* 2c. Then save sysConfig only record */
            sysConfig = sysConfigRepository.save(
                    new SysConfig_Entity(
                            1L,
                            sessionRepository.save(creationSession)
                    ));
        } else {
            /* 3.  If Yes, just update session's ttl */
            Session_Entity creationSession = sysConfig.getCreationSession();
            creationSession.setTtl(data.ttl());
            sessionRepository.save(creationSession);
        }

        /* 4.   Return response */
        return sysConfigMapper.toResponseDTO(
                sysConfig,
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
        SysConfig_Entity sysConfig = sysConfigRepository.findById(1L)
                .orElseThrow(() -> new ServiceUnavailableException(
                        "System Configuration (sysConfig) record is not yet initialized!"
                ));

        /* 3.   If Yes, return response */
        return sysConfigMapper.toResponseDTO(
                sysConfig,
                skillRepository.fetchAllSkillNames()
        );
    }

}
