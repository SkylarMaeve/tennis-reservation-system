package skylarmaeve.tennisreservationsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.dto.CourtDto;
import skylarmaeve.tennisreservationsystem.exception.CourtException;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.util.List;

@Service
@Transactional
public class CourtService {

    private final CourtDao courtDao;
    private final SurfaceTypeDao surfaceTypeDao;

    public CourtService(CourtDao courtDao, SurfaceTypeDao surfaceTypeDao) {
        this.courtDao = courtDao;
        this.surfaceTypeDao = surfaceTypeDao;
    }

    public CourtDto create(CourtDto dto) {
        if (courtDao.findByCourtNumber(dto.getCourtNumber()).isPresent()) {
            throw new CourtException("Court number: "  + dto.getCourtNumber() + " is already is use");
        }

        SurfaceType surfaceType = surfaceTypeDao.findById(dto.getSurfaceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Selected Surface Type does not exist."));

        Court court = new Court();
        court.setSurfaceType(surfaceType);
        court.setCourtNumber(dto.getCourtNumber());

        Court saved =  courtDao.save(court);
        return new CourtDto(saved);
    }

    public List<CourtDto> findAll() {
        return courtDao.findAll().stream().map(CourtDto::new).toList();
    }

    public CourtDto update(CourtDto dto) {
        Court  court = courtDao.findById(dto.getCourtNumber())
                .orElseThrow(() -> new IllegalArgumentException("Selected Court does not exist."));

        SurfaceType surfaceType = surfaceTypeDao.findById(dto.getSurfaceTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Selected Surface Type does not exist."));

        court.setSurfaceType(surfaceType);
        court.setCourtNumber(dto.getCourtNumber());

        Court saved =  courtDao.save(court);
        return new CourtDto(saved);
    }

    public void delete(Long id) {
        Court court = courtDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Selected Court does not exist."));
        courtDao.delete(court);
    }
}
