package nkemrocks.anyteam_v1.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Session_Entity {

    @Id
    @Column(updatable = false, nullable = false)
    UUID id = UuidCreator.getTimeOrderedEpoch();;

    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void createTimeStamps(){

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
    private String sessionName;

    /** Time-To-Live in seconds */
    @NonNull
    @Column(nullable = false)
    private Integer ttl;
    

    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToMany(mappedBy = "session")
    private ArrayList<Stats_Entity> stats = new ArrayList<>();

    @OneToMany(mappedBy = "session")
    private ArrayList<SkillRating_Entity> skillRatings = new ArrayList<>();

    @OneToMany(mappedBy = "session")
    private ArrayList<SkillSelection_Entity> skillSelections = new ArrayList<>();

    
    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Session_Entity that = (Session_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
