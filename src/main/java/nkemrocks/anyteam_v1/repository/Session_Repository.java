package nkemrocks.anyteam_v1.repository;

import nkemrocks.anyteam_v1.entity.Session_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Session_Repository extends JpaRepository<Session_Entity, UUID> {
}
