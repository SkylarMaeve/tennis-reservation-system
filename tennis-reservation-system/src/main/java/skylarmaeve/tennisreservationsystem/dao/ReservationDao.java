package skylarmaeve.tennisreservationsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    @PersistenceContext
    private EntityManager em;

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        }
        return em.merge(reservation);
    }

    public Optional<Reservation> findById(long id) {
        Reservation reservation = em.find(Reservation.class, id);
        return (reservation != null && !reservation.isDeleted()) ? Optional.of(reservation) : Optional.empty();
    }

    public void delete(Reservation reservation) {
        reservation.setDeleted(true);
        em.merge(reservation);
    }

    public List<Reservation> findByCourtNumber(Integer courtNumber) {
        String textQuery = "SELECT reservation " +
                "FROM Reservation reservation " +
                "WHERE reservation.court.courtNumber = :courtNumber AND reservation.deleted = false " +
                "ORDER BY reservation.createdAt ASC";
        var query = em.createQuery(textQuery, Reservation.class)
                .setParameter("courtNumber", courtNumber);
        return query.getResultList();
    }

    public boolean isFreeTimeSlot(Integer courtNumber, LocalDateTime startTime, LocalDateTime endTime) {
        return isFreeTimeSlot(courtNumber, startTime, endTime, null);
    }

    public boolean isFreeTimeSlot(Integer courtNumber, LocalDateTime startTime, LocalDateTime endTime, Long oldReservationId) {
        StringBuilder textQuery = new StringBuilder(
                "SELECT reservation " +
                        "FROM Reservation reservation " +
                        "WHERE reservation.court.courtNumber = :courtNumber " +
                        "AND reservation.startTime < :endTime " +
                        "AND reservation.endTime > :startTime " +
                        "AND reservation.deleted = false"

        );
        if (oldReservationId != null) {
            textQuery.append(" AND reservation.id != :oldReservationId ");
        }

        var query = em.createQuery(textQuery.toString(), Reservation.class)
                .setParameter("courtNumber", courtNumber)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime);

        if (oldReservationId != null) {
            query.setParameter("oldReservationId", oldReservationId);
        }

        return query.getResultList().isEmpty();
    }


    public List<Reservation> findByPhoneNumber(String phoneNumber, boolean onlyFuture) {
        StringBuilder textQuery = new StringBuilder(
                "SELECT reservation " +
                        "FROM Reservation reservation " +
                        "WHERE reservation.customer.phoneNumber = :phoneNumber AND reservation.deleted = false"
        );
        if (onlyFuture) {
            textQuery.append(" AND reservation.startTime > :now");
        }

        var query = em.createQuery(textQuery.toString(), Reservation.class)
                .setParameter("phoneNumber", phoneNumber);
        if (onlyFuture) {
            query.setParameter("now", LocalDateTime.now());
        }
        return query.getResultList();
    }

    public List<Reservation> findAll() {
        String textQuery = "SELECT r " +
                "FROM Reservation r  " +
                "WHERE r.deleted = false";

        return em.createQuery(textQuery, Reservation.class).getResultList();
    }


}
