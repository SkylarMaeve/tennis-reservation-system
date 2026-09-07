package skylarmaeve.tennisreservationsystem.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import skylarmaeve.tennisreservationsystem.dao.AppUserDao;
import skylarmaeve.tennisreservationsystem.model.user.AppUser;
import skylarmaeve.tennisreservationsystem.model.user.Role;

@Component
public class UserInitializer implements CommandLineRunner {

    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(AppUserDao appUserDao, PasswordEncoder passwordEncoder) {
        this.appUserDao = appUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
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
