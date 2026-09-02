package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.Court;

@Getter
@Setter
public class CourtDto {
    private Integer courtNumber;
    private Long surfaceTypeId;
    private String surfaceTypeName;

    public CourtDto(Court court) {
        this.courtNumber = court.getCourtNumber();
        this.surfaceTypeId = court.getSurfaceType().getId();
        this.surfaceTypeName = court.getSurfaceType().getName();
    }
}
