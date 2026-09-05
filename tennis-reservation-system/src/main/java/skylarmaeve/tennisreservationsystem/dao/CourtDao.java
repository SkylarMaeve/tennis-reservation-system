package skylarmaeve.tennisreservationsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CourtDao {
    @PersistenceContext
    private EntityManager em;

    public Court save(Court court) {
        if (court.getId() == null) {
            em.persist(court);
            return court;
        }
        return em.merge(court);
    }

    public Optional<Court> findById(long id) {
        Court court = em.find(Court.class, id);
        return (court != null && !court.isDeleted()) ? Optional.of(court) : Optional.empty();
    }

    public Integer courtUsedInFuture(long courtId) {
        String textQuery = "SELECT reservation " +
                "FROM Reservation reservation " +
                "WHERE reservation.court.id = :courtId AND reservation.deleted = false " +
                "AND reservation.startTime > :now";
        var query = em.createQuery(textQuery, Reservation.class)
                .setParameter("courtId", courtId)
                .setParameter("now", LocalDateTime.now());
        return query.getResultList().size();
    }

    public BigDecimal courtUsedInFutureCost(long courtId) {
        String textQuery = "SELECT SUM(reservation.price)" +
                "FROM Reservation reservation " +
                "WHERE reservation.court.id = :courtId AND reservation.deleted = false " +
                "AND reservation.startTime > :now";

        BigDecimal result = em.createQuery(textQuery, BigDecimal.class)
                .setParameter("courtId", courtId)
                .setParameter("now", LocalDateTime.now())
                .getSingleResult();

        return result != null ? result : BigDecimal.ZERO;
    }

    public Optional<Court> findByCourtNumber(Integer courtNumber) {
        String textQuery = "SELECT c " +
                "FROM Court c  " +
                "WHERE c.deleted = false AND c.courtNumber = :courtNumber";
        var query = em.createQuery(textQuery).setParameter("courtNumber", courtNumber);
        Court court = (Court) query.getSingleResultOrNull();
        return (court != null && !court.isDeleted()) ? Optional.of(court) : Optional.empty();
    }


    public void delete(Court court) {
        court.setDeleted(true);
        em.merge(court);
    }

    public List<Court> findAll() {
        String textQuery = "SELECT c " +
                "FROM Court c  " +
                "WHERE c.deleted = false";

        return em.createQuery(textQuery, Court.class).getResultList();
    }
}
