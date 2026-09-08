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
import skylarmaeve.tennisreservationsystem.dto.SurfaceTypeDto;
import skylarmaeve.tennisreservationsystem.service.SurfaceTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/surfaces")
public class SurfaceTypeController {
    private final SurfaceTypeService surfaceTypeService;

    public SurfaceTypeController(SurfaceTypeService surfaceTypeService) {
        this.surfaceTypeService = surfaceTypeService;
    }

    @PostMapping
    public ResponseEntity<SurfaceTypeDto> createSurface(@RequestBody SurfaceTypeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surfaceTypeService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurfaceTypeDto> getSurface(@PathVariable Long id) {
        return ResponseEntity.ok(surfaceTypeService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<SurfaceTypeDto>> getAllSurfaces() {
        return ResponseEntity.ok(surfaceTypeService.getALl());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurfaceTypeDto> updateSurface(
            @PathVariable Long id,
            @RequestBody SurfaceTypeDto dto) {
        return ResponseEntity.ok(surfaceTypeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurface(@PathVariable Long id) {
        surfaceTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
