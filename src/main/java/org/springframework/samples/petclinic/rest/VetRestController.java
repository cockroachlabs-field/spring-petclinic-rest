/*
 * Copyright 2016-2018 the original author or authors.
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

import java.util.ArrayList;
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

import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Vitaliy Fedoriv
 *
 */

@CrossOrigin(exposedHeaders = "errors, content-type")
@Path("api/vets")
public class VetRestController {

	@Inject
	private ClinicService clinicService;

	@Inject
	private Validator validator;

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@GET
	@Path("")
	@Produces ( MediaType.APPLICATION_JSON)
	public Response getAllVets(){
		Collection<Vet> vets = new ArrayList<Vet>();
		vets.addAll(this.clinicService.findAllVets());
		if (vets.isEmpty()){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(vets).build();
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@GET
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVet(@PathParam("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(vet).build();
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addVet(@Valid Vet vet) { //, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		Set<ConstraintViolation<Vet>> errors = validator.validate(vet);
		if (!errors.isEmpty() || (vet == null)) {
			return Response.status(Status.BAD_REQUEST).entity(vet).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		this.clinicService.saveVet(vet);
		// headers.setLocation(ucBuilder.path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri()); //TODO
		return Response.status(Status.CREATED).entity(vet).build();
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@PUT
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateVet(@PathParam("vetId") int vetId, @Valid Vet vet) { //}, BindingResult bindingResult){
		Set<ConstraintViolation<Vet>> errors = validator.validate(vet);
		if (!errors.isEmpty() || (vet == null)) {
			return Response.status(Status.BAD_REQUEST).entity(vet).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		Vet currentVet = this.clinicService.findVetById(vetId);
		if(currentVet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		currentVet.setFirstName(vet.getFirstName());
		currentVet.setLastName(vet.getLastName());
		currentVet.clearSpecialties();
		for(Specialty spec : vet.getSpecialties()) {
			currentVet.addSpecialty(spec);
		}
		this.clinicService.saveVet(currentVet);
		return Response.noContent().entity(currentVet).build();
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@DELETE
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response deleteVet(@PathParam("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		this.clinicService.deleteVet(vet);
		return Response.noContent().build();
	}



}
