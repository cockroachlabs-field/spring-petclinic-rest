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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.security.Roles;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Path("api/visits")
public class VisitRestController {

	@Inject
	private ClinicService clinicService;

	@Inject
	private Validator validator;

	@RolesAllowed(Roles.OWNER_ADMIN) 
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getAllVisits(){
		Collection<Visit> visits = new ArrayList<Visit>();
		visits.addAll(this.clinicService.findAllVisits());
		if (visits.isEmpty()){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(visits).build();
	}

	@RolesAllowed(Roles.OWNER_ADMIN) 
	@GET
	@Path("/{visitId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getVisit(@PathParam("visitId") int visitId){
		Visit visit = this.clinicService.findVisitById(visitId);
		if(visit == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(visit).build();
	}

	@RolesAllowed(Roles.OWNER_ADMIN) 
	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response addVisit(@Valid Visit visit) { //}, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		Set<ConstraintViolation<Visit>> errors = validator.validate(visit);
		if (!errors.isEmpty() || (visit == null)) {
			return Response.status(Status.BAD_REQUEST).entity(visit).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		this.clinicService.saveVisit(visit);
		// headers.setLocation(ucBuilder.path("/api/visits/{id}").buildAndExpand(visit.getId()).toUri()); // TODO
		return Response.status(Status.CREATED).entity(visit).build();
	}

	@RolesAllowed(Roles.OWNER_ADMIN) 
	@PUT
	@Path("/{visitId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response updateVisit(@PathParam("visitId") int visitId, @Valid Visit visit) { //}, BindingResult bindingResult){
		Set<ConstraintViolation<Visit>> errors = validator.validate(visit);
		if (!errors.isEmpty() || (visit == null)) {
			return Response.status(Status.BAD_REQUEST).entity(visit).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		Visit currentVisit = this.clinicService.findVisitById(visitId);
		if(currentVisit == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		currentVisit.setDate(visit.getDate());
		currentVisit.setDescription(visit.getDescription());
		currentVisit.setPet(visit.getPet());
		this.clinicService.saveVisit(currentVisit);
		return Response.noContent().entity(currentVisit).build();
	}

	@RolesAllowed(Roles.OWNER_ADMIN) 
	@DELETE
	@Path("/{visitId}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public Response deleteVisit(@PathParam("visitId") int visitId){
		Visit visit = this.clinicService.findVisitById(visitId);
		if(visit == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		this.clinicService.deleteVisit(visit);
		return Response.noContent().build();
	}

}
