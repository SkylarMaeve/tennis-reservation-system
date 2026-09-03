package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ReservationResponseDto {
    private final Long id;
    private final Integer courtNumber;
    private final String customerName;
    private final String customerPhoneNumber;

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private final boolean isDoubles;
    private final BigDecimal price;

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
