package skylarmaeve.tennisreservationsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.Customer;

import java.util.Optional;
@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager em;

    public Customer save(Customer customer) {
        if  (customer.getId() == null) {
            em.persist(customer);
            return customer;
        }
        return em.merge(customer);
    }

    public Optional<Customer> findById(long id) {
        Customer customer = em.find(Customer.class, id);
        return (customer != null && !customer.isDeleted()) ? Optional.of(customer): Optional.empty();
    }

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        StringBuilder textQuery = new StringBuilder(
                "SELECT c " +
                "FROM Customer c  " +
                "WHERE c.deleted = false AND c.phoneNumber = :phoneNumber"
        );

        var query = em.createQuery(textQuery.toString()).setParameter("phoneNumber", phoneNumber);
        Customer customer = (Customer) query.getSingleResultOrNull();
        return (customer != null && !customer.isDeleted()) ? Optional.of(customer): Optional.empty();
    }

    public void delete(Customer reservation) {
        reservation.setDeleted(true);
        em.merge(reservation);
    }
}
