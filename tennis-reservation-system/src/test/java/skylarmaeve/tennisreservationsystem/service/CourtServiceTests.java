package skylarmaeve.tennisreservationsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.dto.CourtDto;
import skylarmaeve.tennisreservationsystem.exception.CourtException;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtServiceTests {

    @Mock
    private SurfaceTypeDao surfaceTypeDao;
    @Mock
    private CourtDao courtDao;

    @InjectMocks
    private CourtService courtService;


    @Test
    public void testCreate() {
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        SurfaceType savedSurface = new SurfaceType();
        savedSurface.setId(1L);
        savedSurface.setName("grass");
        savedSurface.setPricePerMinute(BigDecimal.valueOf(10));

        Court saved = new Court();
        saved.setId(1L);
        saved.setCourtNumber(1);
        saved.setSurfaceType(savedSurface);

        when(courtDao.findByCourtNumber(1)).thenReturn(Optional.empty());
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(savedSurface));
        when(courtDao.save(any(Court.class))).thenReturn(saved);

        var result = courtService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getSurfaceTypeId());
        assertEquals("grass", result.getSurfaceTypeName());
    }

    @Test
    public void testCreateCourtNumberUsed() {
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        SurfaceType savedSurface = new SurfaceType();
        savedSurface.setId(1L);
        savedSurface.setName("grass");
        savedSurface.setPricePerMinute(BigDecimal.valueOf(10));

        Court saved = new Court();
        saved.setId(1L);
        saved.setCourtNumber(1);
        saved.setSurfaceType(savedSurface);

        when(courtDao.findByCourtNumber(1)).thenReturn(Optional.of(saved));
        assertThrows(CourtException.class, () -> courtService.create(dto));
    }

    @Test
    public void testCreateSurfaceDoesNotExist() {
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        when(courtDao.findByCourtNumber(1)).thenReturn(Optional.empty());
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourtException.class, () -> courtService.create(dto));

    }

    @Test
    public void testGet() {
        SurfaceType savedSurface = new SurfaceType();
        savedSurface.setId(1L);
        savedSurface.setName("grass");
        savedSurface.setPricePerMinute(BigDecimal.valueOf(10));

        Court saved = new Court();
        saved.setId(1L);
        saved.setCourtNumber(1);
        saved.setSurfaceType(savedSurface);

        when(courtDao.findById(1L)).thenReturn(Optional.of(saved));

        var result = courtService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getSurfaceTypeId());
        assertEquals("grass", result.getSurfaceTypeName());
    }

    @Test
    public void testGetError() {
        when(courtDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourtException.class, () -> courtService.get(1L));
    }

    @Test
    public void testUpdate() {
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(2);
        dto.setSurfaceTypeId(1L);

        SurfaceType savedSurface = new SurfaceType();
        savedSurface.setId(1L);
        savedSurface.setName("grass");
        savedSurface.setPricePerMinute(BigDecimal.valueOf(10));

        Court saved = new Court();
        saved.setId(1L);
        saved.setCourtNumber(1);
        saved.setSurfaceType(savedSurface);

        Court updated = new Court();
        updated.setId(1L);
        updated.setCourtNumber(2);
        updated.setSurfaceType(savedSurface);

        when(courtDao.findById(1)).thenReturn(Optional.of(saved));
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(savedSurface));
        when(courtDao.save(any(Court.class))).thenReturn(updated);

        var result = courtService.update(1L, dto);

        assertNotNull(result);
        assertEquals(2, result.getCourtNumber());
    }

    @Test
    public void testDeleteError() {

        SurfaceType savedSurface = new SurfaceType();
        savedSurface.setId(1L);
        savedSurface.setName("grass");
        savedSurface.setPricePerMinute(BigDecimal.valueOf(10));

        Court saved = new Court();
        saved.setId(1L);
        saved.setCourtNumber(1);
        saved.setSurfaceType(savedSurface);


        when(courtDao.findById(1)).thenReturn(Optional.of(saved));
        when(courtDao.courtUsedInFuture(1L)).thenReturn(1);

        when(courtDao.courtUsedInFutureCost(1L)).thenReturn(BigDecimal.valueOf(10));

        assertThrows(CourtException.class, () -> courtService.delete(1L));
    }
}


