package skylarmaeve.tennisreservationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skylarmaeve.tennisreservationsystem.dto.PriceResponseDto;
import skylarmaeve.tennisreservationsystem.dto.ReservationRequestDto;
import skylarmaeve.tennisreservationsystem.dto.ReservationResponseDto;
import skylarmaeve.tennisreservationsystem.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<PriceResponseDto> createReservation(@RequestBody ReservationRequestDto dto) {
        var price = reservationService.createReservation(dto).getPrice();
        return ResponseEntity.status(HttpStatus.CREATED).body(new PriceResponseDto(price));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationRequestDto dto) {
        var response = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("court/{courtNumber}")
    public ResponseEntity<List<ReservationResponseDto>> getReservation(@PathVariable Integer courtNumber) {
        return ResponseEntity.ok(reservationService.getReservationsByCourtNumber(courtNumber));
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByPhone(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "false") boolean onlyFuture) {
        return ResponseEntity.ok(reservationService.getReservationsByPhoneNumber(phoneNumber, onlyFuture));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
