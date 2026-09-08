package skylarmaeve.tennisreservationsystem.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.tennisreservationsystem.util.ModelFactory;
import skylarmaeve.tennisreservationsystem.model.user.AppUser;
import skylarmaeve.tennisreservationsystem.model.user.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({AppUserDao.class})
public class AppUserDaoTests {

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveAndFindByIdTrue(){
        AppUser user = ModelFactory.makeAppUser("test_user", Role.ROLE_USER);
        user = appUserDao.save(user);

        assertNotNull(user.getId());

        assertTrue(appUserDao.findById(user.getId()).isPresent());
    }

    @Test
    void testFindByIdFalse(){
        assertTrue(appUserDao.findById(547).isEmpty());
    }

    @Test
    void testFindAll(){
        AppUser user = ModelFactory.makeAppUser("test_user", Role.ROLE_USER);
        user = appUserDao.save(user);

        var users = appUserDao.findAll();
        assertEquals(1, users.size());
    }


    @Test
    void testFindByUsername(){
        AppUser user = ModelFactory.makeAppUser("test_user", Role.ROLE_USER);
        user = appUserDao.save(user);

        assertTrue(appUserDao.findByUsername(user.getUsername()).isPresent());
        assertFalse(appUserDao.findByUsername("bob").isPresent());
    }

    @Test
    void testSoftDelete()
    {
        AppUser user = ModelFactory.makeAppUser("test_user", Role.ROLE_USER);
        user = appUserDao.save(user);

        appUserDao.delete(user);

        assertTrue(appUserDao.findById(user.getId()).isEmpty());

        AppUser databaseUser = entityManager.find(AppUser.class, user.getId());
        assertNotNull(databaseUser);
        assertTrue(databaseUser.isDeleted());
    }
}
