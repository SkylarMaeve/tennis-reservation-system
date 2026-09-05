package skylarmaeve.tennisreservationsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.dto.SurfaceTypeDto;
import skylarmaeve.tennisreservationsystem.exception.SurfaceException;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.util.List;

@Service
@Transactional
public class SurfaceTypeService {
    private final SurfaceTypeDao surfaceTypeDao;

    public SurfaceTypeService(SurfaceTypeDao surfaceTypeDao) {
        this.surfaceTypeDao = surfaceTypeDao;
    }

    public SurfaceTypeDto create(SurfaceTypeDto dto) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(dto.getSurfaceTypeName());
        surfaceType.setPricePerMinute(dto.getPricePerMinute());
        SurfaceType saved = surfaceTypeDao.save(surfaceType);
        return new SurfaceTypeDto(saved);
    }

    public SurfaceTypeDto get(Long id) {
        SurfaceType surfaceType = surfaceTypeDao.findById(id)
                .orElseThrow(() -> new SurfaceException("Surface type not found"));

        return new SurfaceTypeDto(surfaceType);
    }

    public List<SurfaceTypeDto> getALl() {
        return surfaceTypeDao.findAll().stream().map(SurfaceTypeDto::new).toList();
    }

    public SurfaceTypeDto update(Long id, SurfaceTypeDto dto) {
        SurfaceType surfaceType = surfaceTypeDao.findById(id)
                .orElseThrow(() -> new SurfaceException("Surface type not found"));

        surfaceType.setName(dto.getSurfaceTypeName());
        surfaceType.setPricePerMinute(dto.getPricePerMinute());

        SurfaceType saved = surfaceTypeDao.save(surfaceType);
        return new SurfaceTypeDto(saved);
    }

    public void delete(Long id) {
        SurfaceType surfaceType = surfaceTypeDao.findById(id)
                .orElseThrow(() -> new SurfaceException("Selected Surface does not exist."));

        int courts = surfaceTypeDao.surfaceTypeUsed(surfaceType.getId());
        if (courts != 0) {
            throw new SurfaceException("SurfaceType is used in: " + courts + " courts.");
        }
        surfaceTypeDao.delete(surfaceType);
    }
}
