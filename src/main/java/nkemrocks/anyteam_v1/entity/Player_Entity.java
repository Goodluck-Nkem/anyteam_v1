package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__players__user_name;

@Entity
@Table(name = "players", uniqueConstraints = {
        @UniqueConstraint(
                name = UK__players__user_name,
                columnNames = {"user_name"})
})
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
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToMany(mappedBy = "player")
    private List<SkillRating_Entity> skillRatings = new ArrayList<>();

    @ManyToMany(mappedBy = "players")
    private Set<Stats_Entity> stats = new HashSet<>();


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
