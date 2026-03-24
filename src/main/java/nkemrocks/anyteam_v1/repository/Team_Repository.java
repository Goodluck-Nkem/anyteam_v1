package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Team_Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface Team_Repository extends JpaRepository<Team_Entity, UUID> {

    Optional<Team_Entity> findByTeamName(String teamName);

    @Modifying
    @Query("""
            UPDATE Team_Entity t
            SET t.rating = :newRating
            WHERE t.id = :teamId
            """)
    int updateTeamRating(UUID teamId, Integer newRating);

    @Query("""
            SELECT t FROM Team_Entity t
            WHERE t.teamName LIKE CONCAT('%', :nameContent, '%')
            """)
    Slice<Team_Entity> findByTeamNameContaining(String nameContent, Pageable pageable);

    @Query("""
            SELECT t FROM Team_Entity t
            """)
    Slice<Team_Entity> findAllTeams(Pageable pageable);

}
