package org.springframework.samples.petclinic.service.userService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.UserService;

public abstract class AbstractUserServiceTests {

    @Inject
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddUser() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEnabled(true);
        user.addRole("OWNER_ADMIN");

        userService.saveUser(user);
        assertThat(user.getRoles().parallelStream().allMatch(role -> role.getName().startsWith("ROLE_")), is(true));
        assertThat(user.getRoles().parallelStream().allMatch(role -> role.getUser() != null), is(true));
    }
}
