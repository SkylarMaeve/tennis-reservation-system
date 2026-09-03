package skylarmaeve.tennisreservationsystem.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import skylarmaeve.tennisreservationsystem.model.user.AppUser;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserDao {
    @PersistenceContext
    private EntityManager em;

    public AppUser save(AppUser user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        }
        return em.merge(user);
    }

    public Optional<AppUser> findById(long id) {
        AppUser user = em.find(AppUser.class, id);
        return (user != null && !user.isDeleted()) ? Optional.of(user) : Optional.empty();
    }

    public Optional<AppUser> findByUsername(String username) {
        String textQuery = "SELECT u " +
                "FROM AppUser u  " +
                "WHERE u.deleted = false AND u.username = :username";

        var query = em.createQuery(textQuery).setParameter("username", username);
        AppUser user = (AppUser) query.getSingleResultOrNull();
        return (user != null && !user.isDeleted()) ? Optional.of(user) : Optional.empty();
    }

    public void delete(AppUser court) {
        court.setDeleted(true);
        em.merge(court);
    }

    public List<AppUser> findAll() {
        String textQuery = "SELECT u " +
                "FROM AppUser u  " +
                "WHERE u.deleted = false";

        return em.createQuery(textQuery, AppUser.class).getResultList();
    }
}
