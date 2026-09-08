package skylarmaeve.tennisreservationsystem.cotrollers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import skylarmaeve.tennisreservationsystem.controller.ReservationController;
import skylarmaeve.tennisreservationsystem.dto.PriceResponseDto;
import skylarmaeve.tennisreservationsystem.dto.ReservationDto;
import skylarmaeve.tennisreservationsystem.service.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTests {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    public void createTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        PriceResponseDto priceResponseDto = new PriceResponseDto(new BigDecimal(15));

        when(reservationService.create(any(ReservationDto.class))).thenReturn(priceResponseDto);
        var response = reservationController.createReservation(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void getIdTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        when(reservationService.get(any(Long.class))).thenReturn(dto);
        var response = reservationController.getReservation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void getAllTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        List<ReservationDto> list = new ArrayList<>();
        list.add(dto);

        when(reservationService.getAll()).thenReturn(list);
        var response = reservationController.getAllReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
    }
    @Test
    public void getByCourtNumberTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        List<ReservationDto> list = new ArrayList<>();
        list.add(dto);

        when(reservationService.getReservationsByCourtNumber(any(Integer.class))).thenReturn(list);
        var response = reservationController.getReservationsByCourtNumber(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getByPhoneNumberTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        List<ReservationDto> list = new ArrayList<>();
        list.add(dto);

        when(reservationService.getReservationsByPhoneNumber(any(String.class), any(boolean.class))).thenReturn(list);
        var response = reservationController.getReservationsByPhone("123456789", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void updateTest(){
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        when(reservationService.update(any(Long.class), any(ReservationDto.class))).thenReturn(dto);
        var response = reservationController.updateReservation(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void deleteTest(){
        var response = reservationController.deleteReservation(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
