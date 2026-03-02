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
    
    /* we got prepersist-set, required-set and default-set fields */

    @PrePersist
    public void generate(){

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
    UUID id;

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
    private ArrayList<TeamStats_Entity> teamStats = new ArrayList<>();

    @OneToMany(mappedBy = "session")
    private ArrayList<PlayerStats_Entity> playerStats = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private boolean requiresArt = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresBiology = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresHistory = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresLanguage = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresLogic = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresMath = false;
    
    @Column(nullable = false, updatable = false)
    private boolean requiresMusic = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresSpelling = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresSport = false;

    @Column(nullable = false, updatable = false)
    private boolean requiresTechnology = false;

    
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
