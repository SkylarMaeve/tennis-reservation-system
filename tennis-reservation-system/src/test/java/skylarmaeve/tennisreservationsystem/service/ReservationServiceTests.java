package skylarmaeve.tennisreservationsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.CustomerDao;
import skylarmaeve.tennisreservationsystem.dao.ReservationDao;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.dto.ReservationDto;
import skylarmaeve.tennisreservationsystem.exception.ReservationException;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;
import skylarmaeve.tennisreservationsystem.model.Reservation;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {


    @Mock
    private SurfaceTypeDao surfaceTypeDao;
    @Mock
    private CourtDao courtDao;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void testCreate() {
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setPhoneNumber("123456789");
        dto.setCustomerName("customer");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(true);
        reservation.setPrice(BigDecimal.valueOf(60));


        when(courtDao.findById(1)).thenReturn(Optional.of(court));
        when(reservationDao.isFreeTimeSlot(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);
        when(customerDao.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(customer));
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation);

        var result = reservationService.create(dto);
        assertEquals(BigDecimal.valueOf(90).stripTrailingZeros(), result.getPrice().stripTrailingZeros());
    }

    @Test
    public void testCreateNewCustomer() {
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(false);
        dto.setPhoneNumber("123456789");
        dto.setCustomerName("customer");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(false);
        reservation.setPrice(BigDecimal.valueOf(60));


        when(courtDao.findById(1)).thenReturn(Optional.of(court));
        when(reservationDao.isFreeTimeSlot(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);
        when(customerDao.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());
        when(customerDao.save(any(Customer.class))).thenReturn(customer);
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation);

        var result = reservationService.create(dto);
        assertEquals(BigDecimal.valueOf(60).stripTrailingZeros(), result.getPrice().stripTrailingZeros());
    }

    @Test
    void testCreateErrorTime() {
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(false);
        dto.setPhoneNumber("123456789");
        dto.setCustomerName("customer");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(ReservationException.class, () -> reservationService.create(dto));
    }

    @Test
    void testCreateErrorCourt() {
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(false);
        dto.setPhoneNumber("123456789");
        dto.setCustomerName("customer");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        when(courtDao.findById(1)).thenReturn(Optional.empty());
        assertThrows(ReservationException.class, () -> reservationService.create(dto));

    }

    @Test
    void testCreateErrorOverlap() {
        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(false);
        dto.setPhoneNumber("123456789");
        dto.setCustomerName("customer");
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));

        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        when(courtDao.findById(1)).thenReturn(Optional.of(court));
        when(reservationDao.isFreeTimeSlot(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(false);

        assertThrows(ReservationException.class, () -> reservationService.create(dto));
    }

    @Test
    void testGet() {
        when(reservationDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ReservationException.class, () -> reservationService.get(1L));
    }

    @Test
    void testGetAll() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(false);
        reservation.setPrice(BigDecimal.valueOf(60));

        var list = new ArrayList<Reservation>();
        list.add(reservation);

        when(reservationDao.findAll()).thenReturn(list);

        var result = reservationService.getAll();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCourtNumber());
    }

    @Test
    void testGetByCourtNumber() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(false);
        reservation.setPrice(BigDecimal.valueOf(60));

        var list = new ArrayList<Reservation>();
        list.add(reservation);

        when(reservationDao.findByCourtNumber(any(Integer.class))).thenReturn(list);

        var result = reservationService.getReservationsByCourtNumber(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCourtNumber());
    }

    @Test
    void testGetByPhoneNumber() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(false);
        reservation.setPrice(BigDecimal.valueOf(60));

        var list = new ArrayList<Reservation>();
        list.add(reservation);

        when(reservationDao.findByPhoneNumber(any(String.class), any(boolean.class))).thenReturn(list);

        var result = reservationService.getReservationsByPhoneNumber("123456789", true);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCourtNumber());
    }

    @Test
    void testUpdate() {

        ReservationDto dto = new ReservationDto();
        dto.setCourtNumber(1);
        dto.setDoubles(true);
        dto.setStartTime(LocalDateTime.now().plusHours(1));
        dto.setEndTime(LocalDateTime.now().plusHours(2));


        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        surfaceType.setName("grass");

        Court court = new Court();
        court.setId(1L);
        court.setCourtNumber(1);
        court.setSurfaceType(surfaceType);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("customer");
        customer.setPhoneNumber("123456789");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(LocalDateTime.now().plusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(2));
        reservation.setDoubles(true);
        reservation.setPrice(BigDecimal.valueOf(60));

        when(reservationDao.findById(1L)).thenReturn(Optional.of(reservation));

        when(courtDao.findById(1L)).thenReturn(Optional.of(court));
        when(reservationDao.isFreeTimeSlot(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Long.class))).thenReturn(true);
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation);

        var result = reservationService.update(1L, dto);
        assertEquals(BigDecimal.valueOf(90).stripTrailingZeros(), result.getPrice().stripTrailingZeros());
    }

    @Test
    void testDelete() {
        when(reservationDao.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ReservationException.class, () -> reservationService.delete(1L));
    }


}
