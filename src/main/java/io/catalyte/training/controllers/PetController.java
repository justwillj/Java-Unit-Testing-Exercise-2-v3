package io.catalyte.training.controllers;

import static io.catalyte.training.constants.StringConstants.CONTEXT_PETS;
import static io.catalyte.training.constants.StringConstants.LOGGER_DELETE_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_POST_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_PUT_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_REQUEST_RECEIVED;
import static io.catalyte.training.constants.StringConstants.MESSAGE_OK;
import static io.catalyte.training.constants.StringConstants.SERVER_ERROR;
import static io.catalyte.training.constants.StringConstants.SERVICE_UNAVAILABLE;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.exceptions.ExceptionResponse;
import io.catalyte.training.exceptions.ServiceUnavailable;
import io.catalyte.training.services.PetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@ApiResponses(value = {
    @ApiResponse(code = 500, message = SERVER_ERROR, response = ExceptionResponse.class),
    @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE, response = ServiceUnavailable.class)
})
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
  @ApiOperation("Finds a Customer by Id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Pet.class),
      @ApiResponse(code = 404, message = "NOT FOUND")
  })
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
  @ApiOperation("Gets all pets, or all pets matching an example with pet fields")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = MESSAGE_OK, response = Pet.class)
  })
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
  @ApiOperation("Adds a list of new pets to the database")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "CREATED", response = Pet.class),
      @ApiResponse(code = 400, message = "BAD REQUEST")
  })
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
  @ApiOperation("Adds a new pet to the database")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "CREATED", response = Pet.class),
      @ApiResponse(code = 400, message = "BAD REQUEST")
  })
  public ResponseEntity<Pet> save(@Valid @RequestBody Pet pet) {
    logger.info(new Date() + LOGGER_POST_REQUEST_RECEIVED);

    return new ResponseEntity<>(PetService.addPet(pet), HttpStatus.CREATED);
  }

  /**
   * Update customer by id customer.
   *
   * @param id the id of the pet to be updated from the path variable
   * @param pet the pet's new information from the request body
   * @return the pet  if correctly input
   */
  @PutMapping(value = "/{id}")
  @ApiOperation("Updates a pet by Id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = MESSAGE_OK, response = Pet.class),
      @ApiResponse(code = 404, message = "NOT FOUND"),
      @ApiResponse(code = 400, message = "BAD REQUEST")
  })
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
  @ApiOperation("Deletes a Pet by Id")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "NO CONTENT"),
      @ApiResponse(code = 404, message = "NOT FOUND")
  })
  public void deletePet(@PathVariable Long id) {
    logger.info(new Date() + LOGGER_DELETE_REQUEST_RECEIVED + id);

    PetService.deletePet(id);
  }
}
