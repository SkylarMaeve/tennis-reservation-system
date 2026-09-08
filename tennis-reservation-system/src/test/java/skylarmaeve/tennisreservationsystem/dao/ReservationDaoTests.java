package skylarmaeve.tennisreservationsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.tennisreservationsystem.util.ModelFactory;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;
import skylarmaeve.tennisreservationsystem.model.Reservation;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({CourtDao.class, SurfaceTypeDao.class, ReservationDao.class, CustomerDao.class})
public class ReservationDaoTests {

    @Autowired
    private CourtDao courtDao;
    @Autowired
    private SurfaceTypeDao surfaceTypeDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void testSaveAndFindByIdTrue(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);

        assertNotNull(reservation.getId());

        assertTrue(reservationDao.findById(reservation.getId()).isPresent());
    }

    @Test
    void testIsFreeTimeSlotOld(){

        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);


        assertTrue(reservationDao.isFreeTimeSlot(court.getCourtNumber(), reservation.getStartTime(), reservation.getEndTime(), reservation.getId()));
    }

    @Test
    void testFindByIdFalse(){
        assertTrue(reservationDao.findById(547).isEmpty());
    }

    @Test
    void testFindAll(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);

        var reservations = reservationDao.findAll();
        assertEquals(1, reservations.size());
    }

    @Test
    void testFindByCourtNumber()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);

        var reservations = reservationDao.findByCourtNumber(69);
        assertEquals(1, reservations.size());
    }

    @Test
    void testFindByPhoneNumber()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);

        var reservations = reservationDao.findByPhoneNumber("123456789", false);
        assertEquals(1, reservations.size());
    }

    @Test
    void testFindByPhoneNumberFuture()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservationFirst = ModelFactory.makeReservation(court, customer);
        reservationFirst = reservationDao.save(reservationFirst);

        Reservation reservationSecond = ModelFactory.makeReservation(court, customer);
        reservationSecond.setStartTime(LocalDateTime.now().plusDays(1));
        reservationSecond.setEndTime(LocalDateTime.now().plusHours(25));
        reservationSecond = reservationDao.save(reservationSecond);

        var reservations = reservationDao.findByPhoneNumber("123456789", true);
        assertEquals(1, reservations.size());
        assertEquals(reservationSecond,  reservations.get(0));
    }

    @Test
    void testFreeTimeSlotFalse()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservationFirst = ModelFactory.makeReservation(court, customer);
        reservationFirst = reservationDao.save(reservationFirst);

        Reservation reservationSecond = ModelFactory.makeReservation(court, customer);

        assertFalse(reservationDao.isFreeTimeSlot(69, reservationSecond.getStartTime(), reservationSecond.getEndTime()));
    }

    @Test
    void testFreeTimeSlotTrue()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservationFirst = ModelFactory.makeReservation(court, customer);
        reservationFirst = reservationDao.save(reservationFirst);

        Reservation reservationSecond = ModelFactory.makeReservation(court, customer);
        reservationSecond.setStartTime(LocalDateTime.now().plusDays(1));
        reservationSecond.setStartTime(LocalDateTime.now().plusHours(25));

        assertTrue(reservationDao.isFreeTimeSlot(69, reservationSecond.getStartTime(), reservationSecond.getEndTime()));
    }


    @Test
    void testSoftDelete()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation = reservationDao.save(reservation);

        reservationDao.delete(reservation);

        assertTrue(reservationDao.findByCourtNumber(69).isEmpty());

        Reservation databaseReservation = entityManager.find(Reservation.class, reservation.getId());
        assertNotNull(databaseReservation);
        assertTrue(databaseReservation.isDeleted());
    }

}
