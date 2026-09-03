package skylarmaeve.tennisreservationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "courts")
public class Court extends BaseEntity {
    @Column(nullable = false, unique = true)
    private Integer courtNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "surface_type_id")
    private SurfaceType surfaceType;

}
