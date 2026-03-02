package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.PlayerStats_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerStats_Repository extends JpaRepository<PlayerStats_Entity, UUID> {
}