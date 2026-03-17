package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.entity.Player_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Player_Repository extends JpaRepository<Player_Entity, UUID> {

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
    List<Player_Details_Projection> getDetailsProjectionById(UUID playerId);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.skill sk
            
            WHERE   p.userName = :userName
            """)
    List<Player_Details_Projection> getDetailsProjectionByName(String userName);

    @Query("""
            SELECT p.id FROM Player_Entity p
            WHERE p.userName LIKE CONCAT('%', :nameContent, '%')
            """)
    List<UUID> getIds_SearchByNameContaining(String nameContent);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.skill sk
            
            WHERE   p.id IN :playerIds
            
            ORDER BY p.userName ASC
            """)
    List<Player_Details_Projection> getDetailsProjectionByManyIds(List<UUID> playerIds);

    @Query("""
            SELECT  p.id AS playerId, p.userName AS userName,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    SkillRating_Entity sr
            
            JOIN    sr.player p
            JOIN    sr.skill sk
            
            WHERE   p.userName IN :userNames
            
            ORDER BY p.userName ASC
            """)
    List<Player_Details_Projection> getDetailsProjectionByManyNames(List<String> userNames);

    @Query("""
            SELECT p.id FROM Player_Entity p
            """)
    List<UUID> getIds_findAll();

}