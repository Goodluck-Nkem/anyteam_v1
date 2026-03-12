package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Team_Entity;
import nkemrocks.anyteam_v1.projection.Team_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Team_Repository extends JpaRepository<Team_Entity, UUID> {
    Optional<Team_Entity> findByTeamName(String name);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   t.id = :teamId
            AND     s.id = t.lastActiveSession.id
            """)
    Optional<Team_Details_Projection> getDetailsProjectionById(UUID teamId);

    @Modifying
    @Query("""
            UPDATE Team_Entity t
            SET t.lastActiveSession.id = :sessionId
            WHERE t.id = :teamId
            """)
    int updateActiveSession(UUID teamId, UUID sessionId);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   t.teamName = :teamName
            AND     s.id = t.lastActiveSession.id
            """)
    Optional<Team_Details_Projection> getDetailsProjectionByName(String teamName);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   t.teamName LIKE CONCAT('%', :nameContent, '%')
            AND     s.id = t.lastActiveSession.id
            
            ORDER BY t.teamName ASC
            """)
    List<Team_Details_Projection> getDetailsProjectionByNameContaining(String nameContent);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName, t.dateCreated AS dateCreated,
                    s.sessionName AS sessionName, s.dateCreated AS dateSessionCreated,
                    st.id AS statsId, st.teamScore AS teamScore, st.entropy AS entropy,
                    st.teamRating AS teamRating, st.dateCreated AS dateStatsCreated
            
            FROM    Stats_Entity st
            
            JOIN    st.team t
            JOIN    st.session s
            
            WHERE   s.id = t.lastActiveSession.id
            
            ORDER BY t.teamName ASC
            """)
    List<Team_Details_Projection> getAllDetailsProjection();

}
