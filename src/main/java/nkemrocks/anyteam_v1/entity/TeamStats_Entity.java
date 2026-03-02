package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "teamStats")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class TeamStats_Entity {

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void generate(){

        /* prepersist-set fields */
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.dateCreated = Instant.now();
    }

    /* ---- ++++++++++++++ ---- */
    /* prepersist-set fields */
    /* ---- ++++++++++++++ ---- */
    @Id
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false)
    private Instant dateCreated;


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @Column(nullable = false, updatable = false)
    private int score;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int entropy;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int rating;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session_Entity session;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team_Entity team;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @ManyToMany(mappedBy = "teamStats")
    private ArrayList<Player_Entity> players = new ArrayList<>();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TeamStats_Entity that = (TeamStats_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
