package org.springframework.samples.petclinic.repository.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.Audited;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class JpaUserRepository implements PanacheRepository<User> {

    @Inject
    EntityManager em;

    @Audited
    public void save(User user)  {
        if (this.em.find(User.class, user.getUsername()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }
}
