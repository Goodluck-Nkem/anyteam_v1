package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nkemrocks.anyteam_v1.GlobalUtil;
import nkemrocks.anyteam_v1.dto.sysConfig.SysConfig_DTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import nkemrocks.anyteam_v1.repository.Session_Repository;
import nkemrocks.anyteam_v1.repository.SysConfig_Repository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SysConfig_Service {
    private final SysConfig_Repository sysConfigRepository;
    private final Session_Repository sessionRepository;

    @Transactional
    public SysConfig_Entity setupCreationSession(SysConfig_DTO data){

        /* STEPS:
         * 1.  Check if the <Creation> session has been initialized
         * 2a. If No, create the creation session
         * 2b. Then save sysConfig only record
         * 3.  If Yes, just update session's TTL
         */

        /* 1. Check if the <Creation> session has been initialized */
        boolean createNow = false;
        SysConfig_Entity sysConfig = sysConfigRepository.findById(1L)
                .orElse(null);

        if(sysConfig == null){
            /* 2a. If No, create the creation session */
            Session_Entity creationSession = new Session_Entity(
                    GlobalUtil.creationSessionName,
                    data.TTL()
            );

            /* 2b. Then save sysConfig only record */
            return sysConfigRepository.save(new SysConfig_Entity(1L, sessionRepository.save(creationSession)));
        }
        else {
            /* 3.  If Yes, just update session's TTL */
            Session_Entity creationSession = sysConfig.getCreationSession();
            creationSession.setTTL(data.TTL());
            sessionRepository.save(creationSession);
            return sysConfig;
        }
    }
}
