package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface UserRepository extends PanacheRepository<User> {

    void save(User user) ;
}
