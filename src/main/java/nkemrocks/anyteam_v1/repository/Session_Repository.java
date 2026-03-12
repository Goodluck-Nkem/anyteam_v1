package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Session_Entity;
import nkemrocks.anyteam_v1.projection.Session_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Session_Repository extends JpaRepository<Session_Entity, UUID> {
    Optional<Session_Entity> findBySessionName(String name);

    @Query("""
            SELECT s.id FROM Session_Entity s
            """)
    List<UUID> getIds_findAll();

    @Query("""
            SELECT s FROM Session_Entity s
            WHERE s.sessionName LIKE CONCAT('%', :nameContent, '%')
            """)
    List<Session_Entity> searchBySessionNameContaining(String nameContent);

    @Query("""
            SELECT s.id FROM Session_Entity s
            WHERE s.sessionName LIKE CONCAT('%', :nameContent, '%')
            """)
    List<UUID> getIds_SearchByNameContaining(String nameContent);

    @Query("""
            SELECT  s.id AS sessionId, s.sessionName AS sessionName,
                    s.ttl AS ttl, s.dateCreated AS dateCreated,
                    s.dateUpdated AS dateUpdated, sk.skillName AS skillName
            
            FROM    SkillSelection_Entity ss
            
            JOIN    ss.session s
            JOIN    ss.skill sk
            
            WHERE   s.id = :sessionId
            """)
    List<Session_Details_Projection> getDetailsProjectionById(UUID sessionId);

    @Query("""
            SELECT  s.id AS sessionId, s.sessionName AS sessionName,
                    s.ttl AS ttl, s.dateCreated AS dateCreated,
                    s.dateUpdated AS dateUpdated, sk.skillName AS skillName
            
            FROM    SkillSelection_Entity ss
            
            JOIN    ss.session s
            JOIN    ss.skill sk
            
            WHERE   s.sessionName = :sessionName
            """)
    List<Session_Details_Projection> getDetailsProjectionByName(String sessionName);

    @Query("""
            SELECT  s.id AS sessionId, s.sessionName AS sessionName,
                    s.ttl AS ttl, s.dateCreated AS dateCreated,
                    s.dateUpdated AS dateUpdated, sk.skillName AS skillName
            
            FROM    SkillSelection_Entity ss
            
            JOIN    ss.session s
            JOIN    ss.skill sk
            
            WHERE   s.id IN :sessionIds
            
            ORDER BY s.sessionName ASC
            """)
    List<Session_Details_Projection> getDetailsProjectionByManyIds(List<UUID> sessionIds);

}
