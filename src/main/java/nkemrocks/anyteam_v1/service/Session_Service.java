package nkemrocks.anyteam_v1.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nkemrocks.anyteam_v1.dto.session.request.Session_Create_RequestDTO;
import nkemrocks.anyteam_v1.dto.session.request.Session_Update_RequestDTO;
import nkemrocks.anyteam_v1.dto.session.response.Session_Create_ResponseDTO;
import nkemrocks.anyteam_v1.dto.session.response.Session_Fetch_ResponseDTO;
import nkemrocks.anyteam_v1.dto.session.response.Session_Update_ResponseDTO;
import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.entity.SkillSelection_Entity;
import nkemrocks.anyteam_v1.entity.Skill_Entity;
import nkemrocks.anyteam_v1.exception.ResourceNotFoundException;
import nkemrocks.anyteam_v1.exception.ServiceUnavailableException;
import nkemrocks.anyteam_v1.exception.SessionExpiredException;
import nkemrocks.anyteam_v1.mapper.Session_Mapper;
import nkemrocks.anyteam_v1.projection.Session_Details_Projection;
import nkemrocks.anyteam_v1.repository.Session_Repository;
import nkemrocks.anyteam_v1.repository.SkillSelection_Repository;
import nkemrocks.anyteam_v1.repository.Skill_Repository;
import nkemrocks.anyteam_v1.util.GlobalUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class Session_Service {
    private final Session_Mapper sessionMapper;
    private final Session_Repository sessionRepository;
    private final Skill_Repository skillRepository;
    private final SkillSelection_Repository skillSelectionRepository;

    @Transactional
    public Session_Create_ResponseDTO createSession(Session_Create_RequestDTO data) {

        /* STEPS:
         * 1a. Confirm the <Creation> session has been initialized
         * 1b. Confirm it is still open
         * 2.  Create and save the new session
         * 3a. Fetch all skills into a map for fast lookup
         * 3b. Save the session's skill requirements
         * 4.  Then return the mapped response
         */

        /* 1a. Confirm the <config> session has been initialized */
        Session_Entity configSession = sessionRepository.findBySessionName(GlobalUtil.configSessionName)
                .orElseThrow(() -> new ServiceUnavailableException("System Configuration (sysConfig) session is not yet initialized!"));

        /* 1b. Confirm it is still open */
        if (Instant.now().isAfter(configSession.getDateCreated().plusSeconds(configSession.getTtl())))
            throw new SessionExpiredException("Config session for creating new teams/players/sessions has expired!");

        /* 2.  Create and save the new session */
        Session_Entity session = sessionRepository.save(
                new Session_Entity(
                        data.sessionName(),
                        data.ttl()
                ));

        /* 3a. Fetch all skills into a map for fast lookup */
        Map<String, Skill_Entity> skillsMap = skillRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Skill_Entity::getSkillName,
                        skill -> skill
                ));

        /* 3b. Save the session's skill requirements */
        List<SkillSelection_Entity> skillSelections = new ArrayList<>();
        for (String skillName : data.requirements()) {
            Skill_Entity skill = skillsMap.get(skillName);
            if (skill == null)
                throw new ResourceNotFoundException("""
                        Operation aborted, Skill with name '%s' not found!"""
                        .formatted(skillName));
            skillSelections.add(
                    new SkillSelection_Entity(
                            session,
                            skill
                    ));
        }
        skillSelectionRepository.saveAll(skillSelections);

        /* 4. Then return the mapped response */
        return sessionMapper.toCreate_ResponseDTO(session);
    }

    public Session_Update_ResponseDTO updateSession(Session_Update_RequestDTO data) {
        Session_Entity session = sessionRepository.findById(data.sessionId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Locale.US, "Session with UUID='%s' not found!", data.sessionId())));
        session.setTtl(data.ttl());
        return sessionMapper.toUpdate_ResponseDTO(sessionRepository.save(session));
    }

    public Session_Fetch_ResponseDTO findSession(UUID sessionId) {
        List<Session_Details_Projection> sessionDetails = sessionRepository.getDetailsProjectionById(sessionId);
        if (sessionDetails.isEmpty())
            throw new ResourceNotFoundException(String.format(Locale.US,
                    "Session with UUID='%s' not found!", sessionId));
        return sessionMapper.toFetch_ResponseDTO(sessionDetails);
    }

    public Session_Fetch_ResponseDTO findSession(String sessionName) {
        List<Session_Details_Projection> sessionDetails = sessionRepository.getDetailsProjectionByName(sessionName);
        if (sessionDetails.isEmpty())
            throw new ResourceNotFoundException(String.format(Locale.US,
                    "Session name: '%s' not found!", sessionName));
        return sessionMapper.toFetch_ResponseDTO(sessionDetails);
    }

    public List<Session_Fetch_ResponseDTO> searchForSessions(String nameContent) {
        /* Get all search session details */
        List<Session_Details_Projection> sessionsDetails = sessionRepository.getDetailsProjectionByManyIds(
                sessionRepository.getIds_SearchByNameContaining(nameContent)
        );

        /* group by ID (maintaining stream order) */
        Map<UUID, List<Session_Details_Projection>> detailsMap = sessionsDetails.stream()
                .collect(Collectors.groupingBy(
                        Session_Details_Projection::getSessionId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(sessionId -> sessionMapper.toFetch_ResponseDTO(
                        detailsMap.getOrDefault(sessionId, List.of())
                ))
                .toList();
    }

    public List<Session_Fetch_ResponseDTO> listAllSessions() {
        /* Get all session details */
        List<Session_Details_Projection> sessionsDetails = sessionRepository.getDetailsProjectionByManyIds(
                sessionRepository.getIds_findAll()
        );

        /* group by ID (maintaining stream order) */
        Map<UUID, List<Session_Details_Projection>> detailsMap = sessionsDetails.stream()
                .collect(Collectors.groupingBy(
                        Session_Details_Projection::getSessionId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        /* return response list */
        return detailsMap.keySet().stream()
                .map(sessionId -> sessionMapper.toFetch_ResponseDTO(
                        detailsMap.getOrDefault(sessionId, List.of())
                ))
                .toList();
    }
}
