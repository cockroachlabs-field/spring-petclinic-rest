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

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Vitaliy Fedoriv
 *
 */

@CrossOrigin(exposedHeaders = "errors, content-type") //TODO
@Path("/api/owners")
public class OwnerRestController {

	@Inject
	private ClinicService clinicService;

	@Inject
	private Validator validator;

	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" ) // TODO
	@GET
	@Path("/*/lastname/{lastName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwnersList(@PathParam("lastName") String ownerLastName) {
		if (ownerLastName == null) {
			ownerLastName = "";
		}
		Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
		if (owners.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(owners).status(Status.OK).build();
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwners() {
		Collection<Owner> owners = this.clinicService.findAllOwners();
		if (owners.isEmpty()) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(owners).status(Status.OK).build();
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOwner(@PathParam("ownerId") int ownerId) {
		Owner owner = null;
		owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(owner).status(Status.OK).build();
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOwner(@Valid Owner owner) { //, BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
		Set<ConstraintViolation<Owner>> errors = validator.validate(owner);
		if (!errors.isEmpty() || (owner == null)) {
			return Response.status(Status.BAD_REQUEST).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).entity(owner).build();
		}
		this.clinicService.saveOwner(owner);
		//headers.setLocation(ucBuilder.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri()); // TODO
		return Response.ok(owner).status(Status.CREATED).build();
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@PUT
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOwner(@PathParam("ownerId") int ownerId, @Valid Owner owner) { // ,BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
		Set<ConstraintViolation<Owner>> errors = validator.validate(owner); 
		if (!errors.isEmpty() || (owner == null)) {
			return Response.status(Status.BAD_REQUEST).entity(owner).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		Owner currentOwner = this.clinicService.findOwnerById(ownerId);
		if (currentOwner == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		currentOwner.setAddress(owner.getAddress());
		currentOwner.setCity(owner.getCity());
		currentOwner.setFirstName(owner.getFirstName());
		currentOwner.setLastName(owner.getLastName());
		currentOwner.setTelephone(owner.getTelephone());
		this.clinicService.saveOwner(currentOwner);
		return Response.status(Status.NO_CONTENT).build();
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@DELETE
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response deleteOwner(@PathParam("ownerId") int ownerId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		this.clinicService.deleteOwner(owner);
		return Response.status(Status.NO_CONTENT).build();
	}

}
