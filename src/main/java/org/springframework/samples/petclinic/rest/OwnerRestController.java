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

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vitaliy Fedoriv
 *
 */

@CrossOrigin(exposedHeaders = "errors, content-type") //TODO
@Path("/api/owners")
public class OwnerRestController {

	@Inject
	private ClinicService clinicService;

	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" ) // TODO
	@GET
	@Path("/*/lastname/{lastName}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<Owner>> getOwnersList(@PathParam("lastName") String ownerLastName) {
		if (ownerLastName == null) {
			ownerLastName = "";
		}
		Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
		if (owners.isEmpty()) {
			return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<Owner>> getOwners() {
		Collection<Owner> owners = this.clinicService.findAllOwners();
		if (owners.isEmpty()) {
			return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Owner> getOwner(@PathParam("ownerId") int ownerId) {
		Owner owner = null;
		owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Owner>(owner, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Owner> addOwner(@RequestBody @Valid Owner owner, BindingResult bindingResult,
			UriComponentsBuilder ucBuilder) {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || (owner == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveOwner(owner);
		headers.setLocation(ucBuilder.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
		return new ResponseEntity<Owner>(owner, headers, HttpStatus.CREATED);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@PUT
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Owner> updateOwner(@PathParam("ownerId") int ownerId, @RequestBody @Valid Owner owner,
			BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || (owner == null)) {
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
		}
		Owner currentOwner = this.clinicService.findOwnerById(ownerId);
		if (currentOwner == null) {
			return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
		}
		currentOwner.setAddress(owner.getAddress());
		currentOwner.setCity(owner.getCity());
		currentOwner.setFirstName(owner.getFirstName());
		currentOwner.setLastName(owner.getLastName());
		currentOwner.setTelephone(owner.getTelephone());
		this.clinicService.saveOwner(currentOwner);
		return new ResponseEntity<Owner>(currentOwner, HttpStatus.NO_CONTENT);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@DELETE
	@Path("/{ownerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public ResponseEntity<Void> deleteOwner(@PathParam("ownerId") int ownerId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deleteOwner(owner);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
