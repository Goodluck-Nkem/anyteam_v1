package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Skill_Entity;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Skill_Repository extends JpaRepository<Skill_Entity, Long> {

    Optional<Skill_Entity> findBySkillName(String skillName);

    @Query("""
            SELECT s.skillName FROM Skill_Entity s
            """)
    List<String> fetchAllSkillNames();

    @Query("""
            SELECT s.skillName FROM Skill_Entity s
            JOIN s.skillSelections ss
            WHERE ss.session.id = :sessionId
            """)
    List<String> fetchSessionSkillNames(UUID sessionId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.session s
            JOIN    sr.skill sk
            
            WHERE   p.id = :playerId
            AND     s.id = :sessionId
            """)
    List<Player_Details_Projection> getPlayerDetailsByPlayerIdAndSessionId(UUID playerId, UUID sessionId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.session s
            JOIN    sr.skill sk
            
            WHERE   p.id = :playerId
            
            ORDER BY s.sessionName ASC
            """)
    List<Player_Details_Projection> getPlayerDetailsByPlayerId(UUID playerId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.session s
            JOIN    sr.skill sk
            
            WHERE   s.id = :sessionId
            
            ORDER BY p.userName ASC
            """)
    List<Player_Details_Projection> getPlayerDetailsBySessionId(UUID sessionId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.session s
            JOIN    sr.skill sk
            
            ORDER BY s.sessionName ASC, p.userName ASC
            """)
    List<Player_Details_Projection> getAllPlayerDetails();

}
