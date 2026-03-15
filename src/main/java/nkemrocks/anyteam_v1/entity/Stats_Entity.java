package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__stats__team_id__session_id;

@Entity
@Table(name = "stats", uniqueConstraints = {
                @UniqueConstraint(
                        name = UK__stats__team_id__session_id,
                        columnNames = {"team_id", "session_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Stats_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    Long id;

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void createTimeStamp() {

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
    @Column(nullable = false, updatable = false)
    private Integer teamScore;

    @NonNull
    @Column(nullable = false, updatable = false)
    private Integer entropy;

    @NonNull
    @Column(nullable = false, updatable = false)
    private Integer teamRating;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session_Entity session;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team_Entity team;

    @NonNull
    @ManyToMany
    @JoinTable(name = "junction__stats__players",
            joinColumns = @JoinColumn(name = "stats_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<Player_Entity> players;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stats_Entity that = (Stats_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
