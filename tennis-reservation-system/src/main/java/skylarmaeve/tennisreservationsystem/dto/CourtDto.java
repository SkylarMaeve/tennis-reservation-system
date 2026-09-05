package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.Court;

@Getter
@Setter
public class CourtDto {
    private Long courtId;
    private Integer courtNumber;
    private Long surfaceTypeId;
    private String surfaceTypeName;

    public CourtDto() {
    }

    public CourtDto(Court court) {
        this.courtId = court.getId();
        this.courtNumber = court.getCourtNumber();
        this.surfaceTypeId = court.getSurfaceType().getId();
        this.surfaceTypeName = court.getSurfaceType().getName();
    }
}
