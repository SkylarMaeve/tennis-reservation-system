package skylarmaeve.tennisreservationsystem.controller;

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
import skylarmaeve.tennisreservationsystem.dto.ReservationDto;
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
    public ResponseEntity<PriceResponseDto> createReservation(@RequestBody ReservationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationDto dto) {
        var response = reservationService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("court/{courtNumber}")
    public ResponseEntity<List<ReservationDto>> getReservationsByCourtNumber(@PathVariable Integer courtNumber) {
        return ResponseEntity.ok(reservationService.getReservationsByCourtNumber(courtNumber));
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ReservationDto>> getReservationsByPhone(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "false") boolean onlyFuture) {
        return ResponseEntity.ok(reservationService.getReservationsByPhoneNumber(phoneNumber, onlyFuture));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
