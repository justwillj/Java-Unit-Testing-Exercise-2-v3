package io.catalyte.training.services;

import io.catalyte.training.entities.Vaccination;
import java.util.List;

public interface VaccinationService {

  List<Vaccination> getAllVaccinations(Vaccination Vaccination);

  Vaccination getVaccination(Long id);

  List<Vaccination> findVaccinationsByBreed(String breed);

  Integer getVaccinationCountByBreed(String breed);

  List<Vaccination> addVaccinations(List<Vaccination> Vaccinations);

  Vaccination addVaccination(Vaccination Vaccination);

  Vaccination updateVaccinationById(Long id, Vaccination Vaccination);

  void deleteVaccination(Long id);

  void deleteVaccinationByInnoculation(String innoculation);
}
