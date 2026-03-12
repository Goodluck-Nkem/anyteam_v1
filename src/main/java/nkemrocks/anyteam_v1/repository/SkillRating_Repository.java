package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.SkillRating_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRating_Repository extends JpaRepository<SkillRating_Entity, Long> {

}