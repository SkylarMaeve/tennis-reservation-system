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
import org.springframework.web.bind.annotation.RestController;
import skylarmaeve.tennisreservationsystem.dto.CourtDto;
import skylarmaeve.tennisreservationsystem.service.CourtService;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    public ResponseEntity<CourtDto> createCourt(@RequestBody CourtDto dto) {
        var response = courtService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtDto> getCourt(@PathVariable Long id) {
        return ResponseEntity.ok(courtService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<CourtDto>> getAllCourts() {
        return ResponseEntity.ok(courtService.getALl());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtDto> updateCourt(
            @PathVariable Long id,
            @RequestBody CourtDto dto) {
        var response = courtService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
