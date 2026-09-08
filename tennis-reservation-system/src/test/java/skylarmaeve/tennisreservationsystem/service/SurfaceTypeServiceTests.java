package skylarmaeve.tennisreservationsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.dto.SurfaceTypeDto;
import skylarmaeve.tennisreservationsystem.exception.SurfaceException;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurfaceTypeServiceTests {

    @Mock
    private SurfaceTypeDao surfaceTypeDao;

    @InjectMocks
    private SurfaceTypeService surfaceTypeService;

    @Test
    public void testCreate() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        SurfaceType saved = new SurfaceType();
        saved.setId(1L);
        saved.setName("grass");
        saved.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeDao.save(any(SurfaceType.class))).thenReturn(saved);

        SurfaceTypeDto result = surfaceTypeService.create(dto);

        assertNotNull(result);
        assertEquals("grass", result.getSurfaceTypeName());
        assertEquals(BigDecimal.valueOf(10).stripTrailingZeros(), result.getPricePerMinute().stripTrailingZeros());
    }

    @Test
    public void testGet() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        SurfaceType saved = new SurfaceType();
        saved.setId(1L);
        saved.setName("grass");
        saved.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(saved));

        SurfaceTypeDto result = surfaceTypeService.get(1L);

        assertNotNull(result);
        assertEquals("grass", result.getSurfaceTypeName());
        assertEquals(BigDecimal.valueOf(10).stripTrailingZeros(), result.getPricePerMinute().stripTrailingZeros());
    }

    @Test
    public void testGetError() {
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SurfaceException.class, () -> surfaceTypeService.get(1L));
    }

    @Test
    public void testGetAll() {
        when(surfaceTypeDao.findAll()).thenReturn(new ArrayList<>());

        var result = surfaceTypeService.getALl();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdate() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("clay");
        dto.setPricePerMinute(BigDecimal.valueOf(15));

        SurfaceType saved = new SurfaceType();
        saved.setId(1L);
        saved.setName("grass");
        saved.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(saved));
        when(surfaceTypeDao.save(any(SurfaceType.class))).thenReturn(saved);


        SurfaceTypeDto result = surfaceTypeService.update(1L, dto);

        assertNotNull(result);
        assertEquals("clay", result.getSurfaceTypeName());
        assertEquals(BigDecimal.valueOf(15).stripTrailingZeros(), result.getPricePerMinute().stripTrailingZeros());
    }

    @Test
    public void testUpdateError() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("clay");
        dto.setPricePerMinute(BigDecimal.valueOf(15));

        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SurfaceException.class, () -> surfaceTypeService.update(1L, dto));
    }

    @Test
    public void testDeleteIdNotFound() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("clay");
        dto.setPricePerMinute(BigDecimal.valueOf(15));

        SurfaceType saved = new SurfaceType();
        saved.setId(1L);
        saved.setName("grass");
        saved.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(saved));
        when(surfaceTypeDao.surfaceTypeUsed(1L)).thenReturn(0);


        surfaceTypeService.delete(1L);

    }
    @Test
    public void testDeleteUsed() {
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("clay");
        dto.setPricePerMinute(BigDecimal.valueOf(15));

        SurfaceType saved = new SurfaceType();
        saved.setId(1L);
        saved.setName("grass");
        saved.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(saved));
        when(surfaceTypeDao.surfaceTypeUsed(1L)).thenReturn(1);

        assertThrows(SurfaceException.class, ()->surfaceTypeService.delete(1L));

    }
}
