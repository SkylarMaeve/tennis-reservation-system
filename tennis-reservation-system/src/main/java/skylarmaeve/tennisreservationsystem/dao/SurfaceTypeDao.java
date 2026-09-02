package skylarmaeve.tennisreservationsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;

import java.util.Optional;

@Repository
public class SurfaceTypeDao {
    @PersistenceContext
    private EntityManager em;

    public SurfaceType save(SurfaceType surfaceType) {
        if  (surfaceType.getId() == null) {
            em.persist(surfaceType);
            return surfaceType;
        }
        return em.merge(surfaceType);
    }

    public Optional<SurfaceType> findById(long id) {
        SurfaceType surfaceType = em.find(SurfaceType.class, id);
        return (surfaceType != null && !surfaceType.isDeleted()) ? Optional.of(surfaceType): Optional.empty();
    }

    public void delete(SurfaceType surfaceType) {
        surfaceType.setDeleted(true);
        em.merge(surfaceType);
    }
}
