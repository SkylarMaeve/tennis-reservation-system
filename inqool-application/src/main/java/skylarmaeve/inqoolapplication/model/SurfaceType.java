package skylarmaeve.inqoolapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "surface_types")
public class SurfaceType extends BaseEntity{

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private float costPerMinute;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCostPerMinute() {
        return costPerMinute;
    }

    public void setCostPerMinute(float costPerMinute) {
        this.costPerMinute = costPerMinute;
    }
}
