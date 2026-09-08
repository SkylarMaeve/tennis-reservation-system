package skylarmaeve.tennisreservationsystem.cotrollers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import skylarmaeve.tennisreservationsystem.controller.SurfaceTypeController;
import skylarmaeve.tennisreservationsystem.dto.SurfaceTypeDto;
import skylarmaeve.tennisreservationsystem.service.SurfaceTypeService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurfaceTypeControllerTests {

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @InjectMocks
    private SurfaceTypeController surfaceTypeController;

    @Test
    public void createTest(){
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeService.create(any(SurfaceTypeDto.class))).thenReturn(dto);
        var response = surfaceTypeController.createSurface(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void getIdTest(){
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeService.get(any(Long.class))).thenReturn(dto);
        var response = surfaceTypeController.getSurface(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void getAllTest(){
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        List<SurfaceTypeDto> list = new ArrayList<>();
        list.add(dto);

        when(surfaceTypeService.getALl()).thenReturn(list);
        var response = surfaceTypeController.getAllSurfaces();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void updateTest(){
        SurfaceTypeDto dto = new SurfaceTypeDto();
        dto.setSurfaceTypeName("grass");
        dto.setPricePerMinute(BigDecimal.valueOf(10));

        when(surfaceTypeService.update(any(Long.class), any(SurfaceTypeDto.class))).thenReturn(dto);
        var response = surfaceTypeController.updateSurface(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void deleteTest(){
        var response = surfaceTypeController.deleteSurface(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
