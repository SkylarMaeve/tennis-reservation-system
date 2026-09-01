package skylarmaeve.inqoolapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "surface_types")
public class SurfaceType extends BaseEntity{

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private float costPerMinute;

}
