package skylarmaeve.tennisreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDto {

    private Long Id;
    private Integer courtNumber;
    private String customerName;
    private String customerPhoneNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean isDoubles;
}
