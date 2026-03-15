package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.SkillRating_Entity;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRating_Repository extends JpaRepository<SkillRating_Entity, Long> {

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.skill sk
            
            WHERE   p.id = :playerId
            """)
    List<Player_Details_Projection> getPlayerDetailsByPlayerId(UUID playerId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.skill sk
            
            ORDER BY p.userName ASC
            """)
    List<Player_Details_Projection> getAllPlayerDetails();

}