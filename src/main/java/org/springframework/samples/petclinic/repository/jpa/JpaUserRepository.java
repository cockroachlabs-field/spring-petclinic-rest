package org.springframework.samples.petclinic.repository.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.Audited;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class JpaUserRepository implements PanacheRepository<User> {

    @PersistenceContext
    private EntityManager em;

    @Audited
    public void save(User user)  {
        if (this.em.find(User.class, user.getUsername()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }
}
