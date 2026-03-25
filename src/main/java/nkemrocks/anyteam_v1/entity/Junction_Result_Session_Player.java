package nkemrocks.anyteam_v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

import static nkemrocks.anyteam_v1.util.ConstraintRelatedStrings.UK__junction_result_session_player__player_id__session_id;
import static nkemrocks.anyteam_v1.util.GlobalUtil.BATCH_SIZE;

@Entity
@Table(name = "junction_result_session_player", uniqueConstraints = {
        @UniqueConstraint(
                name = UK__junction_result_session_player__player_id__session_id,
                columnNames = {"player_id", "session_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Junction_Result_Session_Player {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "junction_result_session_player__sequence_generator"
    )
    @SequenceGenerator(
            name = "junction_result_session_player__sequence_generator",
            sequenceName = "junc_res_sess_players_seq",
            allocationSize = BATCH_SIZE
    )
    @Column(updatable = false, nullable = false)
    Long id;


    /* ---- ++++++++++++++ ---- */
    /* required-set fields */
    /* ---- ++++++++++++++ ---- */
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private Result_Entity result;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session_Entity session;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player_Entity player;


    /* ---- ++++++++++++++ ---- */
    /* equals and hashcode */
    /* ---- ++++++++++++++ ---- */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Junction_Result_Session_Player that = (Junction_Result_Session_Player) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
