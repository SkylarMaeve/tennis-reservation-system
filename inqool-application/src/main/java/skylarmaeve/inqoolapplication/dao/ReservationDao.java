package skylarmaeve.inqoolapplication.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.inqoolapplication.model.Reservation;
import tools.jackson.databind.ser.jackson.RawSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    @PersistenceContext
    private EntityManager em;

    public Reservation save(Reservation reservation) {
        if  (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        }
        return em.merge(reservation);
    }

    public Optional<Reservation> findById(long id) {
        Reservation reservation = em.find(Reservation.class, id);
        return (reservation != null && !reservation.isDeleted()) ? Optional.of(reservation): Optional.empty();
    }

    public void delete(Reservation reservation) {
        reservation.setDeleted(true);
        em.merge(reservation);
    }

    public List<Reservation> findByCourtNumber(Integer courtNumber) {
        StringBuilder textQuery = new StringBuilder(
                "SELECT reservation " +
                "FROM Reservations reservation " +
                "WHERE reservation.court.courtNumber = :courtNumber AND reservation.deleted = false"+
                "ORDER BY resrvation.createdAt ASC"
        );
        var query = em.createQuery(textQuery.toString(),  Reservation.class)
                .setParameter("courtNumber", courtNumber);
        return query.getResultList();
    }

    public boolean isFreeTimeSlot(Long courtId, LocalDateTime startTime, LocalDateTime endTime)
    {
        StringBuilder textQuery = new StringBuilder(
                "SELECT reservation " +
                "FROM Reservations reservation " +
                "WHERE resrvation.courtId == :courtId " +
                        "AND reservation.startTime < :endTime " +
                        "AND reservation.endTime > :startTime"+
                        "AND reservation.deleted = false"

        );

        var query = em.createQuery(textQuery.toString(),  Reservation.class)
                .setParameter("courtId", courtId)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime);

        return query.getResultList().isEmpty();
    }

    public List<Reservation> findByPhoneNumber(String phoneNumber, boolean onlyFuture) {
        StringBuilder textQuery = new StringBuilder(
                "SELECT reservation " +
                "FROM Reservations reservation " +
                "WHERE reservation.customer.phoneNumber = :phoneNumber AND reservation.deleted = false"
        );
        if (onlyFuture) {
            textQuery.append(" AND reservation.startTime > :now");
        }

        var query = em.createQuery(textQuery.toString(),  Reservation.class)
                    .setParameter("phoneNumber", phoneNumber);
        if (onlyFuture) {
            query.setParameter("now", LocalDateTime.now());
        }
        return query.getResultList();
    }


}
