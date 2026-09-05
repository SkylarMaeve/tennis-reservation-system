package skylarmaeve.tennisreservationsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDto {

    private Long reservationId;
    private Integer courtNumber;
    private String customerName;
    private String phoneNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    @JsonProperty("isDoubles")
    private boolean isDoubles;
    private BigDecimal price;

    public ReservationDto() {
    }

    public ReservationDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.courtNumber = reservation.getCourt().getCourtNumber();
        this.customerName = reservation.getCustomer().getName();
        this.phoneNumber = reservation.getCustomer().getPhoneNumber();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.createdAt = reservation.getCreatedAt();
        this.isDoubles = reservation.isDoubles();
        this.price = reservation.getPrice();
    }
}
