package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "playerStats")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PlayerStats_Entity {

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player_Entity player;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session_Entity session;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int math;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int music;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int art;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int history;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int sport;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int language;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int technology;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int spelling;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int logic;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int biology;

    @NonNull
    @Column(nullable = false, updatable = false)
    private int rating;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStats_Entity that = (PlayerStats_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
