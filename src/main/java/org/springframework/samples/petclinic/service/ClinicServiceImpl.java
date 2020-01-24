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
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.jpa.JpaOwnerRepository;
import org.springframework.samples.petclinic.repository.jpa.JpaPetRepository;
import org.springframework.samples.petclinic.repository.jpa.JpaPetTypeRepository;
import org.springframework.samples.petclinic.repository.jpa.JpaSpecialtyRepository;
import org.springframework.samples.petclinic.repository.jpa.JpaVetRepository;
import org.springframework.samples.petclinic.repository.jpa.JpaVisitRepository;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@ApplicationScoped
public class ClinicServiceImpl implements ClinicService {

    JpaPetRepository petRepository;
    JpaVetRepository vetRepository;
    JpaOwnerRepository ownerRepository;
    JpaVisitRepository visitRepository;
    JpaSpecialtyRepository specialtyRepository;
	JpaPetTypeRepository petTypeRepository;

	@Inject
     public ClinicServiceImpl(
       		 JpaPetRepository petRepository,
    		 JpaVetRepository vetRepository,
    		 JpaOwnerRepository ownerRepository,
    		 JpaVisitRepository visitRepository,
    		 JpaSpecialtyRepository specialtyRepository,
			 JpaPetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository; 
		this.petTypeRepository = petTypeRepository;
    }

	@Override
	@Transactional
	public Collection<Pet> findAllPets()  {
		return petRepository.listAll();
	}

	@Override
	@Transactional
	public void deletePet(Pet pet)  {
		petRepository.delete(pet);
	}

	@Override
	@Transactional
	public Visit findVisitById(Integer visitId)  {
		Visit visit = null;
		try {
			visit = visitRepository.findById(visitId);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return visit;
	}

	@Override
	@Transactional
	public Collection<Visit> findAllVisits()  {
		return visitRepository.listAll();
	}

	@Override
	@Transactional
	public void deleteVisit(Visit visit)  {
		visitRepository.delete(visit);
	}

	@Override
	@Transactional
	public Vet findVetById(Integer id)  {
		Vet vet = null;
		try {
			vet = vetRepository.findById(id);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return vet;
	}

	@Override
	@Transactional
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
	@Transactional
	public Collection<Owner> findAllOwners()  {
		return ownerRepository.listAll();
	}

	@Override
	@Transactional
	public void deleteOwner(Owner owner)  {
		ownerRepository.delete(owner);
	}

	@Override
	@Transactional
	public PetType findPetTypeById(Integer petTypeId) {
		PetType petType = null;
		try {
			petType = petTypeRepository.findById(petTypeId);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return petType;
	}

	@Override
	@Transactional
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
	@Transactional
	public Specialty findSpecialtyById(Integer specialtyId) {
		Specialty specialty = null;
		try {
			specialty = specialtyRepository.findById(specialtyId);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return specialty;
	}

	@Override
	@Transactional
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
	@Transactional
	public Collection<PetType> findPetTypes()  {
		return petRepository.findPetTypes();
	}

	@Override
	@Transactional
	public Owner findOwnerById(Integer id)  {
		Owner owner = null;
		try {
			owner = ownerRepository.findById(id);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return owner;
	}

	@Override
	@Transactional
	public Pet findPetById(Integer id)  {
		Pet pet = null;
		try {
			pet = petRepository.findById(id);
		} catch (Exception e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return pet;
	}

	@Override
	@Transactional
	public void savePet(Pet pet) {
		petRepository.persist(pet);
		
	}

	@Override
	@Transactional
	public void saveVisit(Visit visit)  {
		visitRepository.save(visit);
		
	}

	//@Override
	@Transactional
    //@Cacheable(value = "vets")
	public List<Owner> findOwners()  {
		return ownerRepository.listAll();
	}

	@Override
	@Transactional
	public void saveOwner(Owner owner)  {
		ownerRepository.save(owner);
		
	}

	@Override
	@Transactional
	public Collection<Owner> findOwnerByLastName(String lastName)  {
		return ownerRepository.findByLastName(lastName);
	}

	@Override
	@Transactional
	public Collection<Visit> findVisitsByPetId(Integer petId) {
		return visitRepository.findByPetId(petId);
	}

	@Override
	public Collection<Vet> findVets() {
		// TODO Auto-generated method stub
		return null;
	}
	
	


}
