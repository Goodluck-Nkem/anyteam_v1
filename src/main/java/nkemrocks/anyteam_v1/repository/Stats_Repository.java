package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Stats_Entity;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Stats_Repository extends JpaRepository<Stats_Entity, Long> {
    @Query("""
            SELECT st FROM Stats_Entity st
            WHERE st.team.id = :teamId
            AND st.session.id = :sessionId
            """)
    Optional<Stats_Entity> findByTeamSessionPair(UUID teamId, UUID sessionId);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   t.id = :teamId
            AND     s.id = :sessionId
            """)
    List<Team_Details_Projection> getTeamDetailsByTeamIdAndSessionId(UUID teamId, UUID sessionId);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   t.id = :teamId
            
            ORDER BY s.sessionName ASC
            """)
    List<Team_Details_Projection> getTeamDetailsByTeamId(UUID teamId);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   s.id = :sessionId
            
            ORDER BY t.teamName ASC
            """)
    List<Team_Details_Projection> getTeamDetailsBySessionId(UUID sessionId);


    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            ORDER BY s.sessionName ASC, t.teamName ASC
            """)
    List<Team_Details_Projection> getAllTeamDetails();
}
