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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vitaliy Fedoriv
 *
 */

@CrossOrigin(exposedHeaders = "errors, content-type")
@Path("api/pets")
public class PetRestController {

	@Inject
	private ClinicService clinicService;

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("/{petId}")
	@Produces( MediaType.APPLICATION_JSON)
	public ResponseEntity<Pet> getPet(@PathParam("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Pet>(pet, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<Pet>> getPets(){
		Collection<Pet> pets = this.clinicService.findAllPets();
		if(pets.isEmpty()){
			return new ResponseEntity<Collection<Pet>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Pet>>(pets, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@GET
	@Path("/pettypes")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<PetType>> getPetTypes(){
		return new ResponseEntity<Collection<PetType>>(this.clinicService.findPetTypes(), HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@POST
	@Path( "")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Pet> addPet(@RequestBody @Valid Pet pet, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (pet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Pet>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.savePet(pet);
		headers.setLocation(ucBuilder.path("/api/pets/{id}").buildAndExpand(pet.getId()).toUri());
		return new ResponseEntity<Pet>(pet, headers, HttpStatus.CREATED);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@PUT
	@Path("/{petId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Pet> updatePet(@PathVariable("petId") int petId, @RequestBody @Valid Pet pet, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (pet == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Pet>(headers, HttpStatus.BAD_REQUEST);
		}
		Pet currentPet = this.clinicService.findPetById(petId);
		if(currentPet == null){
			return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
		}
		currentPet.setBirthDate(pet.getBirthDate());
		currentPet.setName(pet.getName());
		currentPet.setType(pet.getType());
		currentPet.setOwner(pet.getOwner());
		this.clinicService.savePet(currentPet);
		return new ResponseEntity<Pet>(currentPet, HttpStatus.NO_CONTENT);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@DELETE
	@Path("/{petId}")
	@Produces (MediaType.APPLICATION_JSON)
	@Transactional
	public ResponseEntity<Void> deletePet(@PathVariable("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deletePet(pet);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}


}
