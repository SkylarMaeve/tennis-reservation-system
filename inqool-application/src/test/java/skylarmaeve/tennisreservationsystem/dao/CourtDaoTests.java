package skylarmaeve.tennisreservationsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(CourtDao.class)
public class CourtDaoTests {
    @Autowired
    private CourtDao courtDao;

    @Autowired
    private TestEntityManager entityManager;

    private SurfaceType grass;

    @BeforeEach
    void setUp() {
        grass = new SurfaceType();
        grass.setName("Grass");
        grass.setCostPerMinute(0.50f);
        entityManager.persist(grass);
        entityManager.flush();
    }


    @Test
    void testSaveAndFindByCourtNumber()
    {
        Court court = new Court();
        court.setCourtNumber(2);
        court.setSurfaceType(grass);

        courtDao.save(court);

        List<Court> savedCourts = courtDao.findAll();
        assertEquals(1, savedCourts.size());
        assertEquals(2, savedCourts.get(0).getCourtNumber());
    }

    @Test
    void testSoftDelete()
    {
        Court court = new Court();
        court.setCourtNumber(1);
        court.setSurfaceType(grass);

        courtDao.save(court);

        courtDao.delete(court);

        List<Court> savedCourts = courtDao.findAll();
        assertEquals(0, savedCourts.size());

        Court databaseCourt = entityManager.find(Court.class, 1);
        assertNotNull(databaseCourt);
        assertTrue(databaseCourt.isDeleted());
    }

}
