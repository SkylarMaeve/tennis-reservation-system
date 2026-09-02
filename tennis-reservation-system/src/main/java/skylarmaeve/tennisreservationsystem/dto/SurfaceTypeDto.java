package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurfaceTypeDto {
    private Long surfaceTypeId;
    private String surfaceTypeName;
    private float pricePerMinute;
}
