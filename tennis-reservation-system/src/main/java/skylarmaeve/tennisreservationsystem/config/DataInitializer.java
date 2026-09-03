package skylarmaeve.tennisreservationsystem.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import skylarmaeve.tennisreservationsystem.dao.AppUserDao;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.SurfaceTypeDao;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;
import skylarmaeve.tennisreservationsystem.model.user.AppUser;
import skylarmaeve.tennisreservationsystem.model.user.Role;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(name = "app.data-initialization.enabled", havingValue = "true")
public class DataInitializer implements CommandLineRunner {

    private final SurfaceTypeDao surfaceTypeDao;
    private final CourtDao courtDao;
    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SurfaceTypeDao surfaceTypeDao, CourtDao courtDao, AppUserDao appUserDao, PasswordEncoder passwordEncoder) {
        this.surfaceTypeDao = surfaceTypeDao;
        this.courtDao = courtDao;
        this.appUserDao = appUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        //Seeding as per Assignment
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

            System.out.println("Database was initialized with default court and surface data.");
        }
        //Seeding User roles
        if (appUserDao.findAll().isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ROLE_ADMIN);
            appUserDao.save(admin);

            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRole(Role.ROLE_USER);
            appUserDao.save(user);

            System.out.println("Database was initialized with default user and admin");
        }
    }
}
