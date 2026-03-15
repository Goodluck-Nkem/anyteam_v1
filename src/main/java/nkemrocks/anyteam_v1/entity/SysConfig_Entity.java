package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sys_config")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class SysConfig_Entity {

    /* we got required-set and default-set fields */

    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @Id
    @NonNull
    @Column(updatable = false, nullable = false)
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creation_session_id", nullable = false)
    private Session_Entity creationSession;

    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @Column(nullable = false, updatable = false)
    private Instant dateCreated = Instant.now();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(id, ((SysConfig_Entity) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
