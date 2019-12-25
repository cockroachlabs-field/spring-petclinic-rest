package org.springframework.samples.petclinic.repository.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;

@ApplicationScoped
public class JpaUserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(User user)  {
        if (this.em.find(User.class, user.getUsername()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }
}
