package nkemrocks.anyteam_v1.dto.team.response;

public record Team_PlayerSummary_DTO(
        String playerName,
        int oldAverage,
        int newAverage
) {
}
