package skylarmaeve.inqoolapplication.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.inqoolapplication.model.Court;
import skylarmaeve.inqoolapplication.model.Reservation;

import java.util.List;
import java.util.Optional;

@Repository
public class CourtDao {
    @PersistenceContext
    private EntityManager em;

    public Court save(Court court) {
        if  (court.getId() == null) {
            em.persist(court);
            return court;
        }
        return em.merge(court);
    }

    public Optional<Court> findById(long id) {
        Court court = em.find(Court.class, id);
        return (court != null && !court.isDeleted()) ? Optional.of(court): Optional.empty();
    }

    public void delete(Court court) {
        court.setDeleted(true);
        em.merge(court);
    }

    public List<Court> findAll() {
        StringBuilder textQuery = new StringBuilder(
                "SELECT c " +
                "FROM Court c  " +
                "WHERE c.deleted = false"
        );

        return em.createQuery(textQuery.toString(), Court.class).getResultList();
    }
}
