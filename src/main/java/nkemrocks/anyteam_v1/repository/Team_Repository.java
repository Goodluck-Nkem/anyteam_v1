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
    
    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName,
                    t.dateCreated AS dateCreated, t.passwordHash AS passwordHash,
                    r.teamRating AS teamRating
            
            FROM    Result_Entity r
            
            JOIN    r.team t
            
            WHERE   t.id = :teamId
            AND     r.session.id = t.lastActiveSession.id
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
            SELECT  t.id AS teamId, t.teamName AS teamName,
                    t.dateCreated AS dateCreated, t.passwordHash AS passwordHash,
                    r.teamRating AS teamRating
            
            FROM    Result_Entity r
            
            JOIN    r.team t
            
            WHERE   t.teamName = :teamName
            AND     r.session.id = t.lastActiveSession.id
            """)
    Optional<Team_Details_Projection> getDetailsProjectionByName(String teamName);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName,
                    t.dateCreated AS dateCreated, t.passwordHash AS passwordHash,
                    r.teamRating AS teamRating
            
            FROM    Result_Entity r
            
            JOIN    r.team t
            
            WHERE   t.teamName LIKE CONCAT('%', :nameContent, '%')
            AND     r.session.id = t.lastActiveSession.id
            
            ORDER BY t.teamName ASC
            """)
    List<Team_Details_Projection> getDetailsProjectionByNameContaining(String nameContent);

    @Query("""
            SELECT  t.id AS teamId, t.teamName AS teamName,
                    t.dateCreated AS dateCreated, t.passwordHash AS passwordHash,
                    r.teamRating AS teamRating
            
            FROM    Result_Entity r
            
            JOIN    r.team t
            
            WHERE   r.session.id = t.lastActiveSession.id
            
            ORDER BY t.teamName ASC
            """)
    List<Team_Details_Projection> getAllDetailsProjection();

}
