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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({CourtDao.class, SurfaceTypeDao.class, ReservationDao.class, CustomerDao.class})
public class CourtDaoTests {

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

        assertNotNull(court.getId());

        assertTrue(courtDao.findById(court.getId()).isPresent());
    }

    @Test
    void testFindByIdFalse(){
        assertTrue(courtDao.findById(547).isEmpty());
    }

    @Test
    void testFindAll(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        var courts = courtDao.findAll();
        assertEquals(1, courts.size());
    }

    @Test
    void testCourtUsedInFuture(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(25));
        reservation = reservationDao.save(reservation);


        assertEquals(1, courtDao.courtUsedInFuture(court.getId()));
    }

    @Test
    void testCourtUsedInFutureCost(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 1);
        court = courtDao.save(court);

        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        Reservation reservation = ModelFactory.makeReservation(court, customer);
        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(25));

        reservation = reservationDao.save(reservation);

        assertEquals(reservation.getPrice().stripTrailingZeros(), courtDao.courtUsedInFutureCost(court.getId()).stripTrailingZeros());
    }


    @Test
    void testFindByCourtNumber()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 69);
        court = courtDao.save(court);

        assertTrue(courtDao.findByCourtNumber(69).isPresent());

    }

    @Test
    void testSoftDelete()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        Court court = ModelFactory.makeCourt(surfaceType, 3);
        court = courtDao.save(court);

        courtDao.delete(court);

        assertTrue(courtDao.findByCourtNumber(3).isEmpty());

        Court databaseCourt = entityManager.find(Court.class, court.getId());
        assertNotNull(databaseCourt);
        assertTrue(databaseCourt.isDeleted());
    }

}
