package skylarmaeve.tennisreservationsystem.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {

    private Long Id;
    private Integer courtNumber;
    private String customerName;
    private String customerPhoneNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isDoubles;
}
