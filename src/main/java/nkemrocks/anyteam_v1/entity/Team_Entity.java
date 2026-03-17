package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__teams__team_name;

@Entity
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(
                name = UK__teams__team_name,
                columnNames = {"team_name"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Team_Entity {

    @Id
    @Column(updatable = false, nullable = false)
    UUID id = UuidCreator.getTimeOrderedEpoch();

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void createTimeStamp(){

        /* prepersist-set fields */
        this.dateCreated = Instant.now();
    }

    /* ---- ++++++++++++++ ---- */
    /* prepersist-set fields */
    /* ---- ++++++++++++++ ---- */
    @Column(nullable = false)
    private Instant dateCreated;

    
    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @Column(name = "team_name", nullable = false)
    private String teamName;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_active_session_id", nullable = false)
    private Session_Entity lastActiveSession;

    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToMany(mappedBy = "team")
    private List<Result_Entity> results = new ArrayList<>();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team_Entity that = (Team_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
