package io.catalyte.training.services;


import io.catalyte.training.entities.Pet;
import io.catalyte.training.exceptions.BadDataResponse;
import io.catalyte.training.exceptions.ResourceNotFound;
import io.catalyte.training.exceptions.ServiceUnavailable;
import io.catalyte.training.repositories.PetRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;


@Service
public class PetServiceImpl implements PetService {

  private final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

  @Autowired
  private PetRepository PetRepository;

  /**
   * This method accepts Pets as an optional parameter. When it is supplied, we create a query by
   * example. Otherwise, we get all Pets.
   *
   * @param Pet - any provided fields will be converted to an exact match AND query
   */
  public List<Pet> queryPets(Pet Pet) {
    try {
      if (Pet.isEmpty()) {
        return PetRepository.findAll();
      } else {
        Example<Pet> PetExample = Example.of(Pet);
        return PetRepository.findAll(PetExample);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Lookup a Pet by id.
   *
   * @param id - the id to lookup
   */
  public Pet getPet(Long id) {

    try {
      Pet pet = PetRepository.findById(id).orElse(null);

      if (pet != null) {
        return pet;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this pint, we did not find the Pet
    throw new ResourceNotFound("Could not locate a Pet with the id: " + id);
  }

  /**
   * Writes multiple new Pets to the database.
   *
   * @param Pets - the Pets to write.
   */
  public List<Pet> addPets(List<Pet> Pets) {
    try {
      return PetRepository.saveAll(Pets);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Writes a new Pet to the database.
   *
   * @param Pet - the Pet to write.
   */
  public Pet addPet(Pet Pet) {
    try {
      return PetRepository.save(Pet);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Update an existing Pet in the database.
   *
   * @param id  - the id of the Pet to update.
   * @param Pet - the Pet information to update.
   */
  public Pet updatePetById(Long id, Pet Pet) {

    // first, check to make sure the id passed matches the id in the Pet passed
    if (!Pet.getId().equals(id)) {
      throw new BadDataResponse("Pet ID must match the ID specified in the URL");
    }

    try {
      Pet PetFromDb = PetRepository.findById(id).orElse(null);

      if (PetFromDb != null) {
        return PetRepository.save(Pet);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this pint, we did not find the Pet
    throw new ResourceNotFound("Could not locate a Pet with the id: " + id);

  }

  /**
   * Delete a Pet from the database.
   *
   * @param id - the id of the Pet to be deleted.
   */
  public void deletePet(Long id) {

    try {
      if (PetRepository.existsById(id)) {
        PetRepository.deleteById(id);
        return;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this pint, we did not find the Pet
    throw new ResourceNotFound("Could not locate a Pet with the id: " + id);
  }
}