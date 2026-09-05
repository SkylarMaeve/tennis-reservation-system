package skylarmaeve.tennisreservationsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.util.List;
import java.util.Optional;

@Repository
public class SurfaceTypeDao {
    @PersistenceContext
    private EntityManager em;

    public SurfaceType save(SurfaceType surfaceType) {
        if (surfaceType.getId() == null) {
            em.persist(surfaceType);
            return surfaceType;
        }
        return em.merge(surfaceType);
    }

    public Optional<SurfaceType> findById(long id) {
        SurfaceType surfaceType = em.find(SurfaceType.class, id);
        return (surfaceType != null && !surfaceType.isDeleted()) ? Optional.of(surfaceType) : Optional.empty();
    }

    public List<SurfaceType> findAll() {
        String textQuery = "SELECT s " +
                "FROM SurfaceType s  " +
                "WHERE s.deleted = false";

        return em.createQuery(textQuery, SurfaceType.class).getResultList();
    }

    public Integer surfaceTypeUsed(long surfaceId) {
        String textQuery = "SELECT court " +
                "FROM Court court " +
                "WHERE court.surfaceType.id = :surfaceId AND court.deleted = false ";
        var query = em.createQuery(textQuery, Court.class)
                .setParameter("surfaceId", surfaceId);
        return query.getResultList().size();
    }

    public void delete(SurfaceType surfaceType) {
        surfaceType.setDeleted(true);
        em.merge(surfaceType);
    }
}
