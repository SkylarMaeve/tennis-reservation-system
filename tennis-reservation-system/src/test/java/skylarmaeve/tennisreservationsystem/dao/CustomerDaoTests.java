package skylarmaeve.tennisreservationsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import skylarmaeve.tennisreservationsystem.util.ModelFactory;
import skylarmaeve.tennisreservationsystem.model.Customer;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({CourtDao.class, SurfaceTypeDao.class, ReservationDao.class, CustomerDao.class})
public class CustomerDaoTests {

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
        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        assertNotNull(customer.getId());

        assertTrue(customerDao.findById(customer.getId()).isPresent());
    }

    @Test
    void testFindByIdFalse(){
        assertTrue(customerDao.findById(547).isEmpty());
    }

    @Test
    void testFindByPhoneNumberTrue(){
        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        assertTrue(customerDao.findByPhoneNumber(customer.getPhoneNumber()).isPresent());
    }

    @Test
    void testFindByPhoneNumberFalse(){
        assertTrue(customerDao.findByPhoneNumber("987654321").isEmpty());
    }

    @Test
    void testSoftDelete()
    {
        Customer customer = ModelFactory.makeCustomer("123456789");
        customer = customerDao.save(customer);

        customerDao.delete(customer);

        assertTrue(customerDao.findById(customer.getId()).isEmpty());

        Customer databaseCustomer = entityManager.find(Customer.class, customer.getId());
        assertNotNull(databaseCustomer);
        assertTrue(databaseCustomer.isDeleted());
    }

}
