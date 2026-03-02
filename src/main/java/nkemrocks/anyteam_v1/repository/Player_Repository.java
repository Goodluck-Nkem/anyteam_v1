package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Player_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Player_Repository extends JpaRepository<Player_Entity, UUID> {
    Optional<Player_Entity> findByPlayerName(String name);
    List<Player_Entity> findByPlayerNameContainingIgnoreCase(String name);
}