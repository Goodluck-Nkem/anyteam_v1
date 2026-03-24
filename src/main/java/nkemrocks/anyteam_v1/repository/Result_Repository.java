package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Result_Entity;
import nkemrocks.anyteam_v1.projection.Result_Details_Projection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Result_Repository extends JpaRepository<Result_Entity, Long> {

    /* --- for ids then details in two queries (used for teams) --- */

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Result_Entity r
            
            JOIN    r.session s
            JOIN    r.team t
            JOIN    r.junctions j
            JOIN    j.player p
            
            WHERE   t.id = :teamId
            AND     s.id = :sessionId
            """)
    Slice<Result_Details_Projection> getResultDetailsByTeamIdAndSessionId(
            UUID teamId,
            UUID sessionId,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Result_Entity r
            
            JOIN    r.session s
            JOIN    r.team t
            JOIN    r.junctions j
            JOIN    j.player p
            
            WHERE   t.teamName = :teamName
            AND     s.sessionName = :sessionName
            """)
    Slice<Result_Details_Projection> getResultDetailsByTeamNameAndSessionName(
            String teamName,
            String sessionName,
            Pageable pageable
    );

    @Query("""
            SELECT r.id
            
            FROM Result_Entity r
            
            JOIN r.session s
            JOIN r.team t
            
            WHERE t.id = :teamId
            """)
    Slice<Long> getIds_findAllByTeamId(UUID teamId, Pageable pageable);

    @Query("""
            SELECT r.id
            
            FROM Result_Entity r
            
            JOIN r.session s
            JOIN r.team t
            
            WHERE t.teamName = :teamName
            """)
    Slice<Long> getIds_findAllByTeamName(String teamName, Pageable pageable);

    @Query("""
            SELECT r.id
            
            FROM Result_Entity r
            
            JOIN r.session s
            JOIN r.team t
            
            WHERE s.id = :sessionId
            """)
    Slice<Long> getIds_findAllBySessionId(UUID sessionId, Pageable pageable);

    @Query("""
            SELECT r.id
            
            FROM Result_Entity r
            
            JOIN r.session s
            JOIN r.team t
            
            WHERE s.sessionName = :sessionName
            """)
    Slice<Long> getIds_findAllBySessionName(String sessionName, Pageable pageable);

    @Query("""
            SELECT r.id
            
            FROM Result_Entity r
            
            JOIN r.session s
            JOIN r.team t
            """)
    Slice<Long> getIds_findAll(Pageable pageable);

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE r.id IN :resultIds
            """)
    List<Result_Details_Projection> getResultDetailsByManyIds(List<Long> resultIds);


    /* --- for details in one query (used for players) --- */

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.id = :playerId
            AND     s.id = :sessionId
            """)
    Slice<Result_Details_Projection> getResultDetailsByPlayerIdAndSessionId(
            UUID playerId,
            UUID sessionId,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.playerName = :playerName
            AND     s.sessionName = :sessionName
            """)
    Slice<Result_Details_Projection> getResultDetailsByPlayerNameAndSessionName(
            String playerName,
            String sessionName,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.id = :playerId
            """)
    Slice<Result_Details_Projection> getResultDetailsByPlayerId(
            UUID playerId,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   p.playerName = :playerName
            """)
    Slice<Result_Details_Projection> getResultDetailsByPlayerName(
            String playerName,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   s.id = :sessionId
            """)
    Slice<Result_Details_Projection> getResultDetailsBySessionId(
            UUID sessionId,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            
            WHERE   s.sessionName = :sessionName
            """)
    Slice<Result_Details_Projection> getResultDetailsBySessionName(
            String sessionName,
            Pageable pageable
    );

    @Query("""
            SELECT  r.id AS resultId, r.score AS score, r.entropy AS entropy,
                    r.dateCreated AS dateResultCreated,
                    s.sessionName AS sessionName, t.teamName AS teamName, p.playerName AS playerName
            
            FROM    Junction_Result_Session_Player j
            
            JOIN    j.session s
            JOIN    j.player p
            JOIN    j.result r
            JOIN    r.team t
            """)
    Slice<Result_Details_Projection> getAllResultDetails(Pageable pageable);
}
