package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Result_Entity;
import nkemrocks.anyteam_v1.projection.Result_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Result_Repository extends JpaRepository<Result_Entity, Long> {

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   t.id = :teamId
            AND     s.id = :sessionId
            """)
    List<Result_Details_Projection> getResultDetailsByTeamIdAndSessionId(UUID teamId, UUID sessionId);

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE r.id IN :resultIds
            
            ORDER BY s.sessionName ASC, t.teamName ASC""")
    List<Result_Details_Projection> getResultDetailsByManyIds(List<Long> resultIds);

    @Query("""
            SELECT r.id FROM Result_Entity r WHERE r.team.id = :teamId""")
    List<Long> getIds_findAllByTeamId(UUID teamId);

    @Query("""
            SELECT r.id FROM Result_Entity r WHERE r.session.id = :sessionId""")
    List<Long> getIds_findAllBySessionId(UUID sessionId);

    @Query("""
            SELECT r.id FROM Result_Entity r""")
    List<Long> getIds_findAll();

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.id = :playerId
            AND     s.id = :sessionId
            """)
    List<Result_Details_Projection> getResultDetailsByPlayerIdAndSessionId(UUID playerId, UUID sessionId);

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.id = :playerId
            
            ORDER BY s.sessionName ASC
            """)
    List<Result_Details_Projection> getResultDetailsByPlayerId(UUID playerId);

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   s.id = :sessionId
            
            ORDER BY p.userName ASC
            """)
    List<Result_Details_Projection> getResultDetailsBySessionId(UUID sessionId);

    @Query("""
            SELECT  r.id AS resultId, r.teamScore AS teamScore, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.userName AS userName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.player p
            JOIN    j.session s
            JOIN    j.result r
            JOIN    r.team t
            
            ORDER BY s.sessionName ASC, p.userName ASC""")
    List<Result_Details_Projection> getAllResultDetails();
}
