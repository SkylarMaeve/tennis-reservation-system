package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.math.BigDecimal;

@Getter
@Setter
public class SurfaceTypeDto {
    private Long surfaceTypeId;
    private String surfaceTypeName;
    private BigDecimal pricePerMinute;

    public SurfaceTypeDto() {
    }

    public SurfaceTypeDto(SurfaceType surfaceType) {
        this.surfaceTypeId = surfaceType.getId();
        this.surfaceTypeName = surfaceType.getName();
        this.pricePerMinute = surfaceType.getPricePerMinute();
    }
}
