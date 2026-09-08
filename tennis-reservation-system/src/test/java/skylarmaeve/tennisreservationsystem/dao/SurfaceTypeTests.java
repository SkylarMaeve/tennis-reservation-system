package skylarmaeve.tennisreservationsystem.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.tennisreservationsystem.util.ModelFactory;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({SurfaceTypeDao.class, CourtDao.class})
public class SurfaceTypeTests {

    @Autowired
    private SurfaceTypeDao surfaceTypeDao;

    @Autowired
    private CourtDao courtDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveAndFindByIdTrue(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        assertNotNull(surfaceType.getId());

        assertTrue(surfaceTypeDao.findById(surfaceType.getId()).isPresent());
    }

    @Test
    void testFindByIdFalse(){
        assertTrue(surfaceTypeDao.findById(547).isEmpty());
    }

    @Test
    void testFindAll(){
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        var surfaceTypes = surfaceTypeDao.findAll();
        assertEquals(1, surfaceTypes.size());
    }


    @Test
    void testSurfaceTypeUsed(){
        SurfaceType surfaceTypeFirst = ModelFactory.makeSurfaceType("grass");
        surfaceTypeFirst = surfaceTypeDao.save(surfaceTypeFirst);

        SurfaceType surfaceTypeSecond = ModelFactory.makeSurfaceType("clay");
        surfaceTypeSecond = surfaceTypeDao.save(surfaceTypeSecond);

        Court court = ModelFactory.makeCourt(surfaceTypeFirst, 1);
        court = courtDao.save(court);

        assertEquals(1, surfaceTypeDao.surfaceTypeUsed(surfaceTypeFirst.getId()));
        assertEquals(0, surfaceTypeDao.surfaceTypeUsed(surfaceTypeSecond.getId()));
    }

    @Test
    void testSoftDelete()
    {
        SurfaceType surfaceType = ModelFactory.makeSurfaceType("grass");
        surfaceType = surfaceTypeDao.save(surfaceType);

        surfaceTypeDao.delete(surfaceType);

        assertTrue(surfaceTypeDao.findById(surfaceType.getId()).isEmpty());

        SurfaceType databaseSurfaceType = entityManager.find(SurfaceType.class, surfaceType.getId());
        assertNotNull(databaseSurfaceType);
        assertTrue(databaseSurfaceType.isDeleted());
    }
}
