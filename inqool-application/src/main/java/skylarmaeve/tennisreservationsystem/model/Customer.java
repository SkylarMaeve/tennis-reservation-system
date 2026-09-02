package skylarmaeve.tennisreservationsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity
{
    @Column(nullable = false,  unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String name;

}
