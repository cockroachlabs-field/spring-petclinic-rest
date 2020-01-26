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

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
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

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.security.Roles;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Path("api/pets")
public class PetRestController {

	@Inject
	ClinicService clinicService;

	@Inject
	Validator validator;

    @RolesAllowed( Roles.OWNER_ADMIN )
    @POST
    @Path( "/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPet(@Valid Pet pet) { //, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
        Set<ConstraintViolation<Pet>> errors = validator.validate(pet);
        if (!errors.isEmpty() || (pet == null)) {
            return Response.status(Status.BAD_REQUEST).entity(pet).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
        }
        this.clinicService.savePet(pet);
        // headers.setLocation(ucBuilder.path("/api/pets/{id}").buildAndExpand(pet.getId()).toUri()); //TODO
        return Response.status(Status.CREATED).entity(pet).build();
    }

	@RolesAllowed( Roles.OWNER_ADMIN )
	@GET
	@Path("/{petId}")
	@Produces( MediaType.APPLICATION_JSON)
	public Response getPet(@PathParam("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(pet).build();
	}

	@RolesAllowed( Roles.OWNER_ADMIN )
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPets(){
		Collection<Pet> pets = this.clinicService.findAllPets();
		if(pets.isEmpty()){
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(pets).build();
	}

	@RolesAllowed( Roles.OWNER_ADMIN )
	@GET
	@Path("/pettypes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPetTypes(){
		return Response.ok(this.clinicService.findPetTypes()).build();
	}

	@RolesAllowed( Roles.OWNER_ADMIN )
	@PUT
	@Path("/{petId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePet(@PathParam("petId") int petId, @Valid Pet pet) { //, BindingResult bindingResult){
		Set<ConstraintViolation<Pet>> errors = validator.validate(pet);
		if (!errors.isEmpty() || (pet == null)) {
			return Response.status(Status.BAD_REQUEST).entity(pet).header("errors", errors.stream().collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage))).build();
		}
		Pet currentPet = this.clinicService.findPetById(petId);
		if(currentPet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		currentPet.setBirthDate(pet.getBirthDate());
		currentPet.setName(pet.getName());
		currentPet.setType(pet.getType());
		currentPet.setOwner(pet.getOwner());
		this.clinicService.savePet(currentPet);
		return Response.noContent().entity(currentPet).build();
	}

	@RolesAllowed( Roles.OWNER_ADMIN )
	@DELETE
	@Path("/{petId}")
	@Produces (MediaType.APPLICATION_JSON)
	@Transactional
	public Response deletePet(@PathParam("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		this.clinicService.deletePet(pet);
		return Response.noContent().build();
	}


}
