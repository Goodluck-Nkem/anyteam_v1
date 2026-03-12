package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.SkillSelection_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillSelection_Repository extends JpaRepository<SkillSelection_Entity, Long> {

    @Query("""
            SELECT  ss.skill.id FROM SkillSelection_Entity ss
            WHERE   ss.session.id = :sessionId
            """)
    List<Long> getSkillIds(UUID sessionId);
}
