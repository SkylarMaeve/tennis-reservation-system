package skylarmaeve.tennisreservationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "surface_types")
public class SurfaceType extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal pricePerMinute;

}
