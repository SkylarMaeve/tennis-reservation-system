package skylarmaeve.tennisreservationsystem.cotrollers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import skylarmaeve.tennisreservationsystem.controller.CourtController;
import skylarmaeve.tennisreservationsystem.dto.CourtDto;
import skylarmaeve.tennisreservationsystem.service.CourtService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtControllerTests {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    @Test
    public void createTest(){
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        when(courtService.create(any(CourtDto.class))).thenReturn(dto);
        var response = courtController.createCourt(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void getIdTest(){
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        when(courtService.get(any(Long.class))).thenReturn(dto);
        var response = courtController.getCourt(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void getAllTest(){
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        List<CourtDto> list = new ArrayList<>();
        list.add(dto);

        when(courtService.getALl()).thenReturn(list);
        var response = courtController.getAllCourts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void updateTest(){
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(1);
        dto.setSurfaceTypeId(1L);

        when(courtService.update(any(Long.class), any(CourtDto.class))).thenReturn(dto);
        var response = courtController.updateCourt(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void deleteTest(){
        var response = courtController.deleteCourt(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
