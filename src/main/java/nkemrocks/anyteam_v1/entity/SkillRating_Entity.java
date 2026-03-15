package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__skill_ratings__player_id__skill_id;

@Entity
@Table(name = "skill_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK__skill_ratings__player_id__skill_id,
                        columnNames = {"player_id", "skill_id"})
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class SkillRating_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    Long id;

    /* we got prepersist-set, required-set and default-set fields */

    /* ---- ++++++++++++++ ---- */
    /* prepersist-set fields */
    /* ---- ++++++++++++++ ---- */


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    private Player_Entity player;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false, updatable = false)
    Skill_Entity skill;

    @NonNull
    @Column(nullable = false)
    Integer skillRating;


    /* ---- ++++++++++++++ ---- */
    /* default-set fields */
    /* ---- ++++++++++++++ ---- */


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SkillRating_Entity that = (SkillRating_Entity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
