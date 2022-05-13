package io.catalyte.training.controllers;

import static io.catalyte.training.constants.StringConstants.CONTEXT_VACCINATIONS;
import static io.catalyte.training.constants.StringConstants.LOGGER_REQUEST_BY_BREED_RECEIVED;
import static io.catalyte.training.constants.StringConstants.LOGGER_REQUEST_RECEIVED;

import io.catalyte.training.entities.Vaccination;
import io.catalyte.training.services.VaccinationService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller holds CRUD methods for the Pet entity
 */
@RestController
@RequestMapping(CONTEXT_VACCINATIONS)
public class VaccinationController {

  private final Logger logger = LoggerFactory.getLogger(VaccinationController.class);

  @Autowired
  private VaccinationService VaccinationService;

  /**
   * gives me all vaccinations if I pass a null vaccination or vaccinations matching an example with
   * non-null vaccination
   *
   * @param vaccination vaccination object which can have null or non-null fields, returns status
   *                    200
   * @return List of vaccinations
   */
  @GetMapping
  public ResponseEntity<List<Vaccination>> getVaccinations(Vaccination vaccination) {
    logger.info(new Date() + LOGGER_REQUEST_RECEIVED);

    return new ResponseEntity<>(VaccinationService.getAllVaccinations(vaccination), HttpStatus.OK);
  }

  /**
   * Gets Vaccination by id.
   *
   * @param id the Vaccination id from the path variable
   * @return the Vaccination with said id
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
    logger.info(new Date() + LOGGER_REQUEST_RECEIVED + id);

    return new ResponseEntity<>(VaccinationService.getVaccination(id), HttpStatus.OK);
  }

  /**
   * Gets Vaccination by id.
   *
   * @param breed the Vaccination id from the path variable
   * @return the Vaccination with said id
   */
  @GetMapping(value = "/breed/{breed}")
  public ResponseEntity<List<Vaccination>> getVaccinationByBreed(@PathVariable String breed) {
    logger.info(new Date() + LOGGER_REQUEST_BY_BREED_RECEIVED + breed);
    return new ResponseEntity<>(VaccinationService.findVaccinationsByBreed(breed), HttpStatus.OK);
  }

  @GetMapping(value = "/breed/{breed}/count")
  public ResponseEntity<Integer> getVaccinationCountByMakeAndModelBreed(
      @PathVariable String breed) {
    logger.info(new Date() + LOGGER_REQUEST_BY_BREED_RECEIVED + breed);
    return new ResponseEntity<>(VaccinationService.getVaccinationCountByBreed(breed),
        HttpStatus.OK);
  }

  @PostMapping(value = "/all")
  public ResponseEntity<List<Vaccination>> saveAll(
      @Valid @RequestBody List<Vaccination> Vaccinations) {
    logger.info(new Date() + " Post request received " + Vaccinations.toString());
    return new ResponseEntity<>(VaccinationService.addVaccinations(Vaccinations),
        HttpStatus.CREATED);
  }

  @PostMapping
  public ResponseEntity<Vaccination> save(@Valid @RequestBody Vaccination Vaccination) {
    logger.info(new Date() + " Post request received " + Vaccination.toString());
    return new ResponseEntity<>(VaccinationService.addVaccination(Vaccination), HttpStatus.CREATED);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Vaccination> updateVaccinationById(@PathVariable Long id,
      @Valid @RequestBody Vaccination Vaccination) {
    logger.info(new Date() + " Update request received for id: " + id);
    return new ResponseEntity<>(VaccinationService.updateVaccinationById(id, Vaccination),
        HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity deleteVaccinationById(@PathVariable Long id) {
    logger.info(new Date() + " Delete request received for id: " + id);
    VaccinationService.deleteVaccination(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }


}
