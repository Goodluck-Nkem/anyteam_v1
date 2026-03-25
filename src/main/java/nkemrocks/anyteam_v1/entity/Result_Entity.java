package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__results__team_id__session_id;
import static nkemrocks.anyteam_v1.util.GlobalUtil.BATCH_SIZE;

@Entity
@Table(name = "results", uniqueConstraints = {
                @UniqueConstraint(
                        name = UK__results__team_id__session_id,
                        columnNames = {"team_id", "session_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Result_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "results_seq_gen")
    @SequenceGenerator(
            name = "results_seq_gen",
            sequenceName = "results_seq",
            allocationSize = BATCH_SIZE
    )
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
    private Integer score;

    @NonNull
    @Column(nullable = false, updatable = false)
    private Integer entropy;

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
    @OneToMany(mappedBy = "result")
    private List<Junction_Result_Session_Player> junctions = new ArrayList<>();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Result_Entity that = (Result_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
