package nkemrocks.anyteam_v1.service;

import lombok.AllArgsConstructor;
import nkemrocks.anyteam_v1.dto.session.request.Session_Create_RequestDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.exception.SessionExpiredException;
import nkemrocks.anyteam_v1.repository.Session_Repository;
import nkemrocks.anyteam_v1.repository.SysConfig_Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static nkemrocks.anyteam_v1.GlobalUtil.*;

@Service
@AllArgsConstructor
public class Session_Service {
    private final Session_Repository sessionRepository;
    private final SysConfig_Repository sysConfigRepository;

    public Session_Entity createSession(Session_Create_RequestDTO data){

        /* STEPS:
         * 1a. Confirm the <Creation> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create the new session
         * 3a. Set the session requirements
         * 3b. Save the session, then return
         */

        /* 1a. Confirm the <Creation> session has been initialized */
        SysConfig_Entity sysConfig = sysConfigRepository.findById(1L)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) record is not yet initialized, hence service unavailable!"));
        Session_Entity creationSession = sysConfig.getCreationSession();

        /* 1b. Confirm it is still open */
        if(Instant.now().isAfter(creationSession.getDateCreated().plusSeconds(creationSession.getTtl())))
            throw new SessionExpiredException("Session for creating new teams/players/sessions have expired!");

        /* 2.  Create the new session */
        Session_Entity session = new Session_Entity(
                data.sessionName(),
                data.ttl()
        );

        /* 3a. Set the session requirements */
        session.setRequiresArt(itemPresent(data.requirements(), ART));
        session.setRequiresBiology(itemPresent(data.requirements(), BIOLOGY));
        session.setRequiresHistory(itemPresent(data.requirements(), HISTORY));
        session.setRequiresLanguage(itemPresent(data.requirements(), LANGUAGE));
        session.setRequiresLogic(itemPresent(data.requirements(), LOGIC));
        session.setRequiresMath(itemPresent(data.requirements(), MATH));
        session.setRequiresMusic(itemPresent(data.requirements(), MUSIC));
        session.setRequiresSpelling(itemPresent(data.requirements(), SPELLING));
        session.setRequiresSport(itemPresent(data.requirements(), SPORT));
        session.setRequiresTechnology(itemPresent(data.requirements(), TECHNOLOGY));

        /* 3b. Save the session, then return */
        return sessionRepository.save(session);
    }
}
