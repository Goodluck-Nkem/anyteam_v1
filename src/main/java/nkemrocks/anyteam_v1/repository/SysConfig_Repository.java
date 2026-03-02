package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.SysConfig_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysConfig_Repository extends JpaRepository<SysConfig_Entity, Long> {
}