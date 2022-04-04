package io.catalyte.training.controllers;

import static io.catalyte.training.constants.StringConstants.CONTEXT_PETS;
import static io.catalyte.training.constants.StringConstants.LOGGER_DELETE_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_POST_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_PUT_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_REQUEST_RECEIVED;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.services.PetService;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller holds CRUD methods for the Pet entity
 */
@RestController
@RequestMapping(CONTEXT_PETS)
public class PetController {

  private final Logger logger = LoggerFactory.getLogger(PetController.class);

  @Autowired
  private PetService PetService;

  /**
   * Gets Pet by id.
   *
   * @param id the Pet's id from the path variable
   * @return the Pet with said id
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Pet> getPet(@PathVariable Long id) {
    logger.info(new Date() + LOGGER_REQUEST_RECEIVED + id);

    return new ResponseEntity<>(PetService.getPet(id), HttpStatus.OK);
  }

  /**
   * gives me all pets if I pass a null pet or pets matching an example with non-null pet
   *
   * @param pet pet object which can have null or non-null fields, returns status 200
   * @return List of pets
   */
  @GetMapping
  public ResponseEntity<List<Pet>> queryPets(Pet pet) {
    logger.info(new Date() + LOGGER_REQUEST_RECEIVED + pet.toString());

    return new ResponseEntity<>(PetService.queryPets(pet), HttpStatus.OK);
  }

  /**
   * Adds a list of pets to the database.
   *
   * @param pets the pets from the request body being added
   * @return a list of pets  correctly added
   */
  @PostMapping(value = "/all")
  public ResponseEntity<List<Pet>> saveAll(@Valid @RequestBody List<Pet> pets) {
    logger.info(new Date() + LOGGER_POST_REQUEST_RECEIVED);

    return new ResponseEntity<>(PetService.addPets(pets), HttpStatus.CREATED);
  }

  /**
   * Adds new pet to the database.
   *
   * @param pet the pet from the request body being added
   * @return the pet if correctly added
   */
  @PostMapping
  public ResponseEntity<Pet> save(@Valid @RequestBody Pet pet) {
    logger.info(new Date() + LOGGER_POST_REQUEST_RECEIVED);

    return new ResponseEntity<>(PetService.addPet(pet), HttpStatus.CREATED);
  }

  /**
   * Update customer by id customer.
   *
   * @param id  the id of the pet to be updated from the path variable
   * @param pet the pet's new information from the request body
   * @return the pet  if correctly input
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Pet> updatePetById(
      @PathVariable Long id, @Valid @RequestBody Pet pet) {
    logger.info(new Date() + LOGGER_PUT_REQUEST_RECEIVED + id);

    return new ResponseEntity<>(PetService.updatePetById(id, pet), HttpStatus.OK);
  }

  /**
   * Delete pet by id.
   *
   * @param id the pet's id from the path variable
   */
  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePet(@PathVariable Long id) {
    logger.info(new Date() + LOGGER_DELETE_REQUEST_RECEIVED + id);

    PetService.deletePet(id);
  }
}
