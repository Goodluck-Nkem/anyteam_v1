package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__skills__skill_name;
import static nkemrocks.anyteam_v1.util.GlobalUtil.BATCH_SIZE;


@Entity
@Table(name = "skills", uniqueConstraints = {
        @UniqueConstraint(
                name = UK__skills__skill_name,
                columnNames = {"skill_name"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Skill_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skills_seq_gen")
    @SequenceGenerator(
            name = "skills_seq_gen",
            sequenceName = "skills_seq",
            allocationSize = BATCH_SIZE
    )
    @Column(updatable = false, nullable = false)
    Long id;

    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @Column(name = "skill_name", nullable = false)
    String skillName;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */
    @OneToMany(mappedBy = "skill")
    List<SkillRating_Entity> playerSkillRatings = new ArrayList<>();

    @OneToMany(mappedBy = "skill")
    List<SkillSelection_Entity> skillSelections = new ArrayList<>();


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Skill_Entity that = (Skill_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
