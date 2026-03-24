package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import nkemrocks.anyteam_v1.entity.Player_Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Player_Repository extends JpaRepository<Player_Entity, UUID> {

    Optional<Player_Entity> findByPlayerName(String playerName);

    @Query("""
            SELECT  p.id
            
            FROM    Player_Entity p
            
            WHERE   p.playerName LIKE CONCAT('%', :nameContent, '%')
            """)
    Slice<UUID> getIds_SearchByNameContaining(String nameContent, Pageable pageable);

    @Query("""
            SELECT p.id FROM Player_Entity p
            """)
    Slice<UUID> getIds_findAll(Pageable pageable);

    @Query("""
            SELECT  p.id AS playerId, p.playerName AS playerName, p.passwordHash AS passwordHash,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    Player_Entity p
            
            JOIN    p.skillRatings sr
            JOIN    sr.skill sk
            
            WHERE   p.id = :playerId
            """)
    List<Player_Details_Projection> getDetailsProjectionById(UUID playerId);

    @Query("""
            SELECT  p.id AS playerId, p.playerName AS playerName, p.passwordHash AS passwordHash,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    Player_Entity p
            
            JOIN    p.skillRatings sr
            JOIN    sr.skill sk
            
            WHERE   p.playerName = :playerName
            """)
    List<Player_Details_Projection> getDetailsProjectionByName(String playerName);

    @Query("""
            SELECT  p.id AS playerId, p.playerName AS playerName, p.passwordHash AS passwordHash,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    Player_Entity p
            
            JOIN    p.skillRatings sr
            JOIN    sr.skill sk
            
            WHERE   p.id IN :playerIds
            """)
    List<Player_Details_Projection> getDetailsProjectionByManyIds(List<UUID> playerIds);

    @Query("""
            SELECT  p.id AS playerId, p.playerName AS playerName, p.passwordHash AS passwordHash,
                    p.firstName AS firstName, p.lastName AS lastName,
                    p.dateCreated AS dateCreated, p.dateUpdated AS dateUpdated,
                    sk.id AS skillId, sk.skillName AS skillName,
                    sr.skillRating AS skillRating
            
            FROM    Player_Entity p
            
            JOIN    p.skillRatings sr
            JOIN    sr.skill sk
            
            WHERE   p.playerName IN :playerNames
            """)
    List<Player_Details_Projection> getDetailsProjectionByManyNames(List<String> playerNames);

}