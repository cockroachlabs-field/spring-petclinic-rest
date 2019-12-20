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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
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

@CrossOrigin(exposedHeaders = "errors, content-type")
@Path("api/vets")
public class VetRestController {

	@Inject
	private ClinicService clinicService;

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@GET
	@Path("")
	@Produces ( MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<Vet>> getAllVets(){
		Collection<Vet> vets = new ArrayList<Vet>();
		vets.addAll(this.clinicService.findAllVets());
		if (vets.isEmpty()){
			return new ResponseEntity<Collection<Vet>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Vet>>(vets, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@GET
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Vet> getVet(@PathParam("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return new ResponseEntity<Vet>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vet>(vet, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Vet> addVet(@RequestBody @Valid Vet vet, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (vet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveVet(vet);
		headers.setLocation(ucBuilder.path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri());
		return new ResponseEntity<Vet>(vet, headers, HttpStatus.CREATED);
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@PUT
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Vet> updateVet(@PathParam("vetId") int vetId, @RequestBody @Valid Vet vet, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (vet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
		}
		Vet currentVet = this.clinicService.findVetById(vetId);
		if(currentVet == null){
			return new ResponseEntity<Vet>(HttpStatus.NOT_FOUND);
		}
		currentVet.setFirstName(vet.getFirstName());
		currentVet.setLastName(vet.getLastName());
		currentVet.clearSpecialties();
		for(Specialty spec : vet.getSpecialties()) {
			currentVet.addSpecialty(spec);
		}
		this.clinicService.saveVet(currentVet);
		return new ResponseEntity<Vet>(currentVet, HttpStatus.NO_CONTENT);
	}

    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@DELETE
	@Path("/{vetId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public ResponseEntity<Void> deleteVet(@PathParam("vetId") int vetId){
		Vet vet = this.clinicService.findVetById(vetId);
		if(vet == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deleteVet(vet);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}



}
