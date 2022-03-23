package io.catalyte.training.services;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.entities.Vaccination;
import io.catalyte.training.exceptions.BadDataResponse;
import io.catalyte.training.exceptions.ResourceNotFound;
import io.catalyte.training.exceptions.ServiceUnavailable;
import io.catalyte.training.repositories.VaccinationRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class VaccinationServiceImpl implements VaccinationService {

  @Autowired
  private VaccinationRepository VaccinationRepository;

  @Autowired
  private PetService PetService;

  /**
   * Reads all Vaccinations from the database.
   *
   * @return - a list of all Vaccinations in the database
   */
  public List<Vaccination> getAllVaccinations(Vaccination Vaccination) {
    try {

      if (Vaccination.isEmpty()) {
        return VaccinationRepository.findAll();
      } else {
        Example<Vaccination> VaccinationExample = Example.of(Vaccination);
        return VaccinationRepository.findAll(VaccinationExample);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }


  /**
   * Looks up a Vaccination from the database by id.
   *
   * @param id - the id of the Vaccination to lookup.
   * @return - the Vaccination belonging to the ID or a 404 if not found
   */
  public Vaccination getVaccination(Long id) {
    try {
      Vaccination VaccinationLookupResult = VaccinationRepository.findById(id).orElse(null);
      if (VaccinationLookupResult != null) {
        return VaccinationLookupResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this pint, we did not find the Vaccination
    throw new ResourceNotFound("Could not locate a Vaccination with the id: " + id);
  }

  /**
   * Writes multiple new Vaccinations to the database.
   *
   * @param Vaccinations - the Vaccinations to write.
   * @return - the list of new Vaccinations
   */
  public List<Vaccination> addVaccinations(List<Vaccination> Vaccinations) {
    try {
      return VaccinationRepository.saveAll(Vaccinations);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Writes a new Vaccination to the database.
   *
   * @param Vaccination - the Vaccination to write.
   * @return - the enw Vaccination
   */
  public Vaccination addVaccination(Vaccination Vaccination) {
    try {
      return VaccinationRepository.save(Vaccination);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Updates the database information for an existing Vaccination. Does not insert a record if the
   * id does not exist.
   *
   * @param id - the id of the Vaccination to update.
   * @param Vaccination - the supplied Vaccination information to update the database record with.
   * @return - the updated Vaccination or a 404 if the Vaccination was not found
   */
  public Vaccination updateVaccinationById(Long id, Vaccination Vaccination) {

    // first, check to make sure the id passed matches the id in the Vaccination passed
    if (!Vaccination.getId().equals(id)) {
      throw new BadDataResponse("Vaccination ID must match the ID specified in the URL");
    }

    try {
      Vaccination VaccinationFromDb = VaccinationRepository.findById(id).orElse(null);
      if (VaccinationFromDb != null) {
        return VaccinationRepository.save(Vaccination);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this point, we did not find the Vaccination
    throw new ResourceNotFound("Could not locate a Vaccination with the id: " + id);
  }

  /**
   * Retrieves all Vaccinations related to a breed of Pet.
   *
   * @param breed - the breed of Pet that Vaccinations should be related to.
   * @return List<Vaccination>
   */
  public List<Vaccination> findVaccinationsByBreed(String breed) {

    // create new Pet to use as an example
    Pet petToQuery = new Pet(null, breed, null);
    try {
      List<Pet> petQueryResults = PetService.queryPets(petToQuery);
      List<Long> idList = new ArrayList<>();

      if (!petQueryResults.isEmpty()) {
        petQueryResults.forEach(Pet -> idList.add(Pet.getId()));
        return VaccinationRepository.findVaccinationsByPetIdIn(idList);
      } else {
        return new ArrayList<>();
      }

    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Returns only the count of Vaccinations related to a certain make and model combination.
   *
   * @param breed - the make of Pet that Vaccinations should be related to.
   * @return an integer that represents the count of vacs for that breed
   */
  public Integer getVaccinationCountByBreed(String breed) {
    List<Vaccination> VaccinationList = findVaccinationsByBreed(breed);

    return VaccinationList.size();
  }

  /**
   * Deletes a Vaccination from the database by id.
   *
   * @param id - the id of the Vaccination to delete from the database.
   */
  public void deleteVaccination(Long id) {

    try {
      if (VaccinationRepository.existsById(id)) {
        VaccinationRepository.deleteById(id);
        return;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this point, we did not find the Vaccination
    throw new ResourceNotFound("Could not locate a Vaccination with the id: " + id);
  }

  /**
   * Deletes Vaccinations from the database by innoculation (disease).
   *
   * @param innoculation - the innoculation of the Vaccinations to delete from the database.
   */
  public void deleteVaccinationByInnoculation(String innoculation) {
    try {
      if (VaccinationRepository.existsByInnoculation(innoculation)) {
        VaccinationRepository.deleteByInnoculation(innoculation);
        return;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if we made it down to this point, we did not find the Vaccination
    throw new ResourceNotFound(
        "Could not locate a Vaccination with the innoculation: " + innoculation);
  }
}
