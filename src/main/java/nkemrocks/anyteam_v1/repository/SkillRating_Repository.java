package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.SkillRating_Entity;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRating_Repository extends JpaRepository<SkillRating_Entity, Long> {
    @Query("""
            SELECT  sr FROM SkillRating_Entity sr
            WHERE   sr.player.id = :playerId
            AND     sr.skill.id = :skillId""")
    Optional<SkillRating_Entity> findByPlayerIdAndSkillId(UUID playerId, Long skillId);
}