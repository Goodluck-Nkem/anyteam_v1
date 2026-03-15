package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Skill_Entity;
import nkemrocks.anyteam_v1.projection.Player_Details_Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Skill_Repository extends JpaRepository<Skill_Entity, Long> {

    @Query("""
            SELECT s.skillName FROM Skill_Entity s
            """)
    List<String> fetchAllSkillNames();

}
