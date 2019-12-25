/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Cacheable;
import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;


/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
public class ClinicServiceImpl implements ClinicService {

    private PetRepository petRepository;
    private VetRepository vetRepository;
    private OwnerRepository ownerRepository;
    private VisitRepository visitRepository;
    private SpecialtyRepository specialtyRepository;
	private PetTypeRepository petTypeRepository;

    @Inject
     public ClinicServiceImpl(
       		 PetRepository petRepository,
    		 VetRepository vetRepository,
    		 OwnerRepository ownerRepository,
    		 VisitRepository visitRepository,
    		 SpecialtyRepository specialtyRepository,
			 PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository; 
		this.petTypeRepository = petTypeRepository;
    }

	@Override
	@Transactional(readOnly = true)
	public Collection<Pet> findAllPets()  {
		return petRepository.listAll();
	}

	@Override
	@Transactional
	public void deletePet(Pet pet)  {
		petRepository.delete(pet);
	}

	@Override
	@Transactional(readOnly = true)
	public Visit findVisitById(int visitId)  {
		Visit visit = null;
		try {
			visit = visitRepository.findById(visitId);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return visit;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Visit> findAllVisits()  {
		return visitRepository.listAll();
	}

	@Override
	@Transactional
	public void deleteVisit(Visit visit)  {
		visitRepository.delete(visit);
	}

	@Override
	@Transactional(readOnly = true)
	public Vet findVetById(int id)  {
		Vet vet = null;
		try {
			vet = vetRepository.findById(id);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return vet;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Vet> findAllVets()  {
		return vetRepository.listAll();
	}

	@Override
	@Transactional
	public void saveVet(Vet vet)  {
		vetRepository.save(vet);
	}

	@Override
	@Transactional
	public void deleteVet(Vet vet)  {
		vetRepository.delete(vet);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Owner> findAllOwners()  {
		return ownerRepository.listAll();
	}

	@Override
	@Transactional
	public void deleteOwner(Owner owner)  {
		ownerRepository.delete(owner);
	}

	@Override
	@Transactional(readOnly = true)
	public PetType findPetTypeById(int petTypeId) {
		PetType petType = null;
		try {
			petType = petTypeRepository.findById(petTypeId);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return petType;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<PetType> findAllPetTypes()  {
		return petTypeRepository.listAll();
	}

	@Override
	@Transactional
	public void savePetType(PetType petType)  {
		petTypeRepository.save(petType);
	}

	@Override
	@Transactional
	public void deletePetType(PetType petType)  {
		petTypeRepository.delete(petType);
	}

	@Override
	@Transactional(readOnly = true)
	public Specialty findSpecialtyById(int specialtyId) {
		Specialty specialty = null;
		try {
			specialty = specialtyRepository.findById(specialtyId);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return specialty;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Specialty> findAllSpecialties()  {
		return specialtyRepository.listAll();
	}

	@Override
	@Transactional
	public void saveSpecialty(Specialty specialty)  {
		specialtyRepository.save(specialty);
	}

	@Override
	@Transactional
	public void deleteSpecialty(Specialty specialty)  {
		specialtyRepository.delete(specialty);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes()  {
		return petRepository.findPetTypes();
	}

	@Override
	@Transactional(readOnly = true)
	public Owner findOwnerById(int id)  {
		Owner owner = null;
		try {
			owner = ownerRepository.findById(id);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return owner;
	}

	@Override
	@Transactional(readOnly = true)
	public Pet findPetById(int id)  {
		Pet pet = null;
		try {
			pet = petRepository.findById(id);
		} catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return pet;
	}

	@Override
	@Transactional
	public void savePet(Pet pet) throws Exception {
		petRepository.save(pet);
		
	}

	@Override
	@Transactional
	public void saveVisit(Visit visit)  {
		visitRepository.save(visit);
		
	}

	@Override
	@Transactional(readOnly = true)
    @Cacheable(value = "vets")
	public Collection<Vet> findVets()  {
		return vetRepository.listAll();
	}

	@Override
	@Transactional
	public void saveOwner(Owner owner) throws Exception {
		ownerRepository.save(owner);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(String lastName)  {
		return ownerRepository.findByLastName(lastName);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
	
	


}
