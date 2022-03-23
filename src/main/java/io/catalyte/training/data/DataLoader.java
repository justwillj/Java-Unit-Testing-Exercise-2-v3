package io.catalyte.training.data;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.entities.Vaccination;
import io.catalyte.training.repositories.PetRepository;
import io.catalyte.training.repositories.VaccinationRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This class runs after the server starts and executes methods that load initial datasets into the
 * db
 */
@Component
public class DataLoader implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  private PetRepository PetRepository;

  @Autowired
  private VaccinationRepository VaccinationRepository;

  private Pet Pet1;
  private Pet Pet2;
  private Pet Pet3;

  @Override
  public void run(String... strings) throws Exception {
    logger.info("Loading data...");

    loadPets();
    loadVaccinations();
  }

  private void loadPets() {
    Pet1 = PetRepository.save(new Pet("Cletus", "Dog", 6));
    Pet2 =
        PetRepository.save(
            new Pet("Alexander Bunnington", "Rabbit", 3));
    Pet3 = PetRepository.save(new Pet("Evie", "Rabbit", 2));
  }

  private void loadVaccinations() {
    VaccinationRepository.save(
        new Vaccination(
            "Rabies",
            Date.from(
                LocalDate.parse("2019-10-17").atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Pet1));
    VaccinationRepository.save(
        new Vaccination(
            "Worms",
            Date.from(
                LocalDate.parse("2020-04-12").atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Pet1));
    VaccinationRepository.save(
        new Vaccination(
            "RVHD",
            Date.from(
                LocalDate.parse("2019-05-10").atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Pet2));
    VaccinationRepository.save(
        new Vaccination(
            "RVHD",
            Date.from(
                LocalDate.parse("2019-06-08").atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Pet3));
  }
}
