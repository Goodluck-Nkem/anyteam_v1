package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Player_Entity {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id = UuidCreator.getTimeOrderedEpoch();

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void createTimeStamps() {

        /* prepersist-set fields */
        Instant now = Instant.now();
        this.dateCreated = now;
        this.dateUpdated = now;

    }

    @PreUpdate
    public void updateTimeStamp(){
        this.dateUpdated = Instant.now();
    }

    /* ---- ++++++++++++++ ---- */
    /* prepersist-set fields */
    /* ---- ++++++++++++++ ---- */
    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    @Column(nullable = false)
    private Instant dateUpdated;


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @Column(nullable = false, unique = true)
    private String userName;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_active_session_id", nullable = false)
    private Session_Entity lastActiveSession;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToMany(mappedBy = "player")
    private List<SkillRating_Entity> skillRatingCollection = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "junction_player_stats",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "stats_id"))
    private List<Stats_Entity> stats = new ArrayList<>();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player_Entity that = (Player_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
