package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__skill_selections__session_id__skill_id;
import static nkemrocks.anyteam_v1.util.GlobalUtil.BATCH_SIZE;

@Entity
@Table(name = "skill_selections", uniqueConstraints = {
        @UniqueConstraint(
                name = UK__skill_selections__session_id__skill_id,
                columnNames = {"session_id", "skill_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class SkillSelection_Entity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skillsel_seq_gen")
    @SequenceGenerator(
            name = "skillsel_seq_gen",
            sequenceName = "skillsel_seq",
            allocationSize = BATCH_SIZE
    )
    @Column(updatable = false, nullable = false)
    Long id;


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    Session_Entity session;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    Skill_Entity skill;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SkillSelection_Entity that = (SkillSelection_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
