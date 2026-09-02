package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.math.BigDecimal;
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
    private BigDecimal price;

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
