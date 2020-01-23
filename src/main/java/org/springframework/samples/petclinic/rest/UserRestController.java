/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.security.Roles;
import org.springframework.samples.petclinic.service.UserService;

@Path("api/users")
public class UserRestController {

    @Inject
    UserService userService;

    @Inject
    Validator validator;
    
	@RolesAllowed(Roles.ADMIN) 
    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOwner( @Valid User user) throws Exception { // }, BindingResult bindingResult) throws Exception {
		Set<ConstraintViolation<User>> errors = validator.validate(user);
		if (!errors.isEmpty() || (user == null)) {
			return Response.status(Status.BAD_REQUEST).entity(user).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}

        userService.saveUser(user);
        return Response.status(Status.CREATED).entity(user).build();
    }
}
