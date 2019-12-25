package org.springframework.samples.petclinic.security;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Roles {

    public final String OWNER_ADMIN = "ROLE_OWNER_ADMIN";
    public final String VET_ADMIN = "ROLE_VET_ADMIN";
    public final String ADMIN = "ROLE_ADMIN";
}
