package skylarmaeve.inqoolapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.inqoolapplication.dao.CourtDao;
import skylarmaeve.inqoolapplication.dao.CustomerDao;
import skylarmaeve.inqoolapplication.dao.ReservationDao;
import skylarmaeve.inqoolapplication.model.Court;
import skylarmaeve.inqoolapplication.model.Customer;
import skylarmaeve.inqoolapplication.model.Reservation;
import skylarmaeve.inqoolapplication.model.SurfaceType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({ReservationDao.class})
public class ReservationDaoTests {
    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TestEntityManager entityManager;

    private SurfaceType grass;
    private Customer customer;
    private Court court;

    @BeforeEach
    void setUp() {
        grass = new SurfaceType();
        grass.setName("Grass");
        grass.setCostPerMinute(0.50f);
        entityManager.persist(grass);
        entityManager.flush();

        customer = new Customer();
        customer.setName("Uriel");
        customer.setPhoneNumber("123456789");
        entityManager.persist(customer);
        entityManager.flush();

        court = new Court();
        court.setCourtNumber(1);
        court.setSurfaceType(grass);
        entityManager.persist(court);
        entityManager.flush();

        entityManager.clear();
    }

    Reservation getReservation() {
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setDoubles(false);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now());
        reservation.setPrice(300f);
        return reservation;
    }

    @Test
    void testFindByPhoneNumber()
    {
        Reservation firstReservation = getReservation();
        Reservation secondReservation = getReservation();

        Reservation differentCustomerReservation = getReservation();
        Customer differentCustomer = new Customer();
        differentCustomer.setName("Azrael");
        differentCustomer.setPhoneNumber("987654321");
        differentCustomerReservation.setCustomer(differentCustomer);
        entityManager.persist(differentCustomer);
        entityManager.flush();

        reservationDao.save(firstReservation);
        reservationDao.save(secondReservation);
        reservationDao.save(differentCustomerReservation);

        List<Reservation> reservations = reservationDao.findByPhoneNumber("123456789", false);
        assertEquals(2, reservations.size());

        List<Reservation> allReservations = reservationDao.findAll();
        assertEquals(3, allReservations.size());
    }
    @Test
    void testFindByPhoneNumberOnlyFuture()
    {
        Reservation firstReservation = getReservation();
        Reservation secondReservation = getReservation();

        firstReservation.setStartTime(LocalDateTime.now().minusDays(1));
        firstReservation.setEndTime(LocalDateTime.now().minusDays(1));

        secondReservation.setStartTime(LocalDateTime.now().plusDays(1));
        secondReservation.setEndTime(LocalDateTime.now().plusDays(1));

        reservationDao.save(firstReservation);
        reservationDao.save(secondReservation);

        List<Reservation> reservations = reservationDao.findByPhoneNumber("123456789", true);
        assertEquals(1, reservations.size());


        List<Reservation> allReservations = reservationDao.findAll();
        assertEquals(2, allReservations.size());

    }

    @Test
    void testSaveAndFindByCourtNumber()
    {
        Reservation firstReservation = getReservation();
        Reservation secondReservation = getReservation();

        Court secondCourt = new Court();
        secondCourt.setCourtNumber(2);
        secondCourt.setSurfaceType(grass);
        entityManager.persist(secondCourt);
        entityManager.flush();

        secondReservation.setCourt(secondCourt);

        reservationDao.save(firstReservation);
        reservationDao.save(secondReservation);

        List<Reservation> reservations = reservationDao.findByCourtNumber(2);
        assertEquals(1, reservations.size());
    }
    @Test
    void testNoOverlap()
    {
        LocalDateTime firstReservationStartTime = LocalDateTime.now();
        LocalDateTime firstReservationEndTime = firstReservationStartTime.plusHours(1);

        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setDoubles(false);
        reservation.setStartTime(firstReservationStartTime);
        reservation.setEndTime(firstReservationEndTime);
        reservation.setPrice(300f);

        reservationDao.save(reservation);
        entityManager.flush();


        assertTrue(reservationDao.isFreeTimeSlot(court.getCourtNumber(), firstReservationEndTime, firstReservationEndTime.plusHours(1)));
    }

    @Test
    void testOverlap()
    {
        LocalDateTime firstReservationStartTime = LocalDateTime.now();
        LocalDateTime firstReservationEndTime = LocalDateTime.now().plusHours(1);

        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setDoubles(false);
        reservation.setStartTime(firstReservationStartTime);
        reservation.setEndTime(firstReservationEndTime);
        reservation.setPrice(300f);

        reservationDao.save(reservation);
        entityManager.flush();

        LocalDateTime secondReservationStartTime = firstReservationStartTime.plusMinutes(30);

        assertFalse(reservationDao.isFreeTimeSlot(court.getCourtNumber(), secondReservationStartTime, firstReservationEndTime));
    }

    @Test
    void testSoftDelete()
    {
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setDoubles(false);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(1));
        reservation.setPrice(300f);

        reservationDao.save(reservation);
        entityManager.flush();

        List<Reservation> savedCourts = reservationDao.findAll();
        assertEquals(1, savedCourts.size());

        reservationDao.delete(reservation);

        Reservation databaseReservation = entityManager.find(Reservation.class, reservation.getId());
        assertNotNull(databaseReservation);
        assertTrue(databaseReservation.isDeleted());
        assertTrue(reservationDao.findAll().isEmpty());
    }

}
