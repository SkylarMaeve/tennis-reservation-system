package skylarmaeve.tennisreservationsystem.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(name = "app.data-initialization.enabled", havingValue = "true")
public class DataInitializer implements CommandLineRunner {

    private final SurfaceTypeDao surfaceTypeDao;
    private final CourtDao courtDao;

    public DataInitializer(SurfaceTypeDao surfaceTypeDao, CourtDao courtDao) {
        this.surfaceTypeDao = surfaceTypeDao;
        this.courtDao = courtDao;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (surfaceTypeDao.findAll().isEmpty()) {

            SurfaceType clay = new SurfaceType();
            clay.setName("Clay");
            clay.setPricePerMinute(new BigDecimal("1.50"));
            clay = surfaceTypeDao.save(clay);

            SurfaceType grass = new SurfaceType();
            grass.setName("Grass");
            grass.setPricePerMinute(new BigDecimal("2.00"));
            grass = surfaceTypeDao.save(grass);


            Court court1 = new Court();
            court1.setCourtNumber(1);
            court1.setSurfaceType(clay);
            courtDao.save(court1);

            Court court2 = new Court();
            court2.setCourtNumber(2);
            court2.setSurfaceType(clay);
            courtDao.save(court2);

            Court court3 = new Court();
            court3.setCourtNumber(3);
            court3.setSurfaceType(grass);
            courtDao.save(court3);

            Court court4 = new Court();
            court4.setCourtNumber(4);
            court4.setSurfaceType(grass);
            courtDao.save(court4);

            System.out.println("Database was initialized with default data.");
        }
    }
}
