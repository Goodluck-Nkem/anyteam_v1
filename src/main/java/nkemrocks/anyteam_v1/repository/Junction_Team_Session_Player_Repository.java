package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Junction_Result_Session_Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Junction_Team_Session_Player_Repository extends JpaRepository<Junction_Result_Session_Player, Long> {
}
