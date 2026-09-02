package skylarmaeve.tennisreservationsystem.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.time.LocalDateTime;
@Getter
public class ReservationResponseDto {
    private Long id;
    private Integer courtNumber;
    private String customerName;
    private String customerPhoneNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isDoubles;
    private float price;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.courtNumber = reservation.getCourt().getCourtNumber();
        this.customerName = reservation.getCustomer().getName();
        this.customerPhoneNumber = reservation.getCustomer().getPhoneNumber();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.isDoubles = reservation.isDoubles();
        this.price = reservation.getPrice();
    }
}
