package skylarmaeve.tennisreservationsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDto {

    private Long Id;
    private Integer courtNumber;
    private String customerName;
    private String phoneNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @JsonProperty("isDoubles")
    private boolean isDoubles;
}
