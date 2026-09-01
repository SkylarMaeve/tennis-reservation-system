package skylarmaeve.inqoolapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "courts")
public class Court extends BaseEntity
{
    @Column(nullable = false,  unique = true)
    private String courtName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "surface_type_id")
    private SurfaceType surfaceType;

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }
}
