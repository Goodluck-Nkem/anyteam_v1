package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Player_Entity {

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void generate() {

        /* prepersist-set fields */
        Instant now = Instant.now();
        this.id = UuidCreator.getTimeOrderedEpoch();
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
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private Instant dateCreated;

    @Column(nullable = false)
    private Instant dateUpdated;


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @Column(nullable = false, unique = true)
    private String playerName;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_playerstats_id", nullable = true)
    private PlayerStats_Entity currentPlayerStats;

    @OneToMany(mappedBy = "player")
    private ArrayList<PlayerStats_Entity> playerStatsForAllSessions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "junction_player_teamstats",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "teamstats_id"))
    private ArrayList<TeamStats_Entity> teamStats = new ArrayList<>();


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
