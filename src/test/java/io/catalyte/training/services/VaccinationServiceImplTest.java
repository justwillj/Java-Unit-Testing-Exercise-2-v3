package io.catalyte.training.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyCollection;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.entities.Vaccination;
import io.catalyte.training.exceptions.BadDataResponse;
import io.catalyte.training.exceptions.ResourceNotFound;
import io.catalyte.training.exceptions.ServiceUnavailable;
import io.catalyte.training.repositories.VaccinationRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;

public class VaccinationServiceImplTest {

  @Mock
  VaccinationRepository vaccinationRepository;

  @Mock
  PetService petService;

  @InjectMocks
  VaccinationServiceImpl vaccinationServiceImpL;

  // member variables for expected results
  Pet testPet;
  Vaccination testVaccination;
  List<Vaccination> testList = new ArrayList<>();

  @BeforeEach
  @SuppressWarnings("unchecked")
  public void setUp() {
    //initialize mocks
    MockitoAnnotations.openMocks(this);

    //set up test Pet
    testPet = new Pet("Whopper", "Dog", 1);
    testPet.setId(1L);

    //set up test Vac
    testVaccination = new Vaccination("Rabies", Date.valueOf("2021-01-08"), testPet);
    testVaccination.setId(1L);
    testList.add(testVaccination);

    when(vaccinationRepository.findById(any(Long.class))).thenReturn(Optional.of(testList.get(0)));
    when(vaccinationRepository.saveAll(anyCollection())).thenReturn(testList);
    when(vaccinationRepository.save(any(Vaccination.class))).thenReturn(testList.get(0));
    when(vaccinationRepository.findAll()).thenReturn(testList);
    when(vaccinationRepository.findAll(any(Example.class))).thenReturn(testList);
  }

  @Test
  public void getAllVaccinations() {
    List<Vaccination> result = vaccinationServiceImpl.getAllVaccinations(new Vaccination());
    assertEquals(testList, result);
  }

  @Test
  public void getAllVaccinationsWithSample() {
    List<Vaccination> result = vaccinationServiceImpl.getAllVaccinations(testVaccination);
    assertEquals(testList, result);
  }

  @Test
  public void getAllVaccinationsDBError() {
    // set repo to trigger Data Access Exception
    when(vaccinationRepository.findAll()).thenThrow(EmptyResultDataAccessException.class);

    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.getAllVaccinations(new Vaccination()));
  }

  @Test
  public void getVaccination() {
    Vaccination result = vaccinationServiceImpl.getVaccination(1L);
    assertEquals(testVaccination, result);
  }

  @Test
  public void getVaccinationDBError() {
    when(vaccinationRepository.findById(anyLong())).thenThrow(EmptyResultDataAccessException.class);
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.getVaccination(1L));
  }

  @Test
  public void getVaccinationNotFound() {
    when(vaccinationRepository.findById(anyLong())).thenReturn(Optional.empty());
    Exception exception = assertThrows(ResourceNotFound.class,
        () -> vaccinationServiceImpl.getVaccination(1L));
    String expectedMessage = "Could not locate a Vaccination with the id: 1";
    assertEquals(expectedMessage,
        exception.getMessage(),
        () -> "Message did not equal '" + expectedMessage + "', actual message:"
            + exception.getMessage());
  }

  @Test
  public void addVaccinations() {
    List<Vaccination> result = vaccinationServiceImpl.addVaccinations(testList);
    assertEquals(testList, result);
  }

  @Test
  public void addVaccinationsDBError() {
    when(vaccinationRepository.saveAll(anyCollection())).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));

    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.addVaccinations(testList));
  }

  @Test
  public void addVaccination() {
    Vaccination result = vaccinationServiceImpl.addVaccination(testVaccination);
    assertEquals(testVaccination, result);
  }

  @Test
  public void addVaccinationDBError() {
    when(vaccinationRepository.save(any(Vaccination.class))).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.addVaccination(testVaccination));
  }

  @Test
  public void updateVaccinationById() {
    Vaccination result = vaccinationServiceImpl.updateVaccinationById(1L, testVaccination);
    assertEquals(testVaccination, result);
  }

  @Test
  public void updateVaccinationByIdDBError() {
    when(vaccinationRepository.findById(anyLong())).thenThrow(EmptyResultDataAccessException.class);
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.updateVaccinationById(1L, testVaccination));
  }

  @Test
  public void updateVaccinationByIdNotFound() {
    when(vaccinationRepository.findById(anyLong())).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFound.class,
        () -> vaccinationServiceImpl.updateVaccinationById(1L, testVaccination));
    String expectedMessage = "Could not locate a Vaccination with the id: 1";
    assertEquals(expectedMessage,
        exception.getMessage(),
        () -> "Message did not equal '" + expectedMessage + "', actual message:"
            + exception.getMessage());
  }

  @Test
  public void updateVaccinationByIdBadData() {
    Exception exception = assertThrows(BadDataResponse.class,
        () -> vaccinationServiceImpl.updateVaccinationById(2L, testVaccination));
    String expectedMessage = "Vaccination ID must match the ID specified in the URL";
    assertEquals(expectedMessage,
        exception.getMessage(),
        () -> "Message did not equal '" + expectedMessage + "', actual message:"
            + exception.getMessage());
  }

  @Test
  public void findVaccinationsByBreed() {
    when(vaccinationRepository.findVaccinationsByPetIdIn(any())).thenReturn(testList);
    when(petService.queryPets(any())).thenReturn(Collections.singletonList(testPet));

    List<Vaccination> result = vaccinationServiceImpl.findVaccinationsByBreed("breed");
    assertEquals(testList, result);
  }

  @Test
  public void findVaccinationsByBreedEmptyResult() {
    when(petService.queryPets(any())).thenReturn(new ArrayList<>());

    List<Vaccination> result = vaccinationServiceImpl.findVaccinationsByBreed("breed");
    assertEquals(new ArrayList<>(), result);
  }

  @Test
  public void findVaccinationsByBreedDBError() {
    when(petService.queryPets(any())).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.findVaccinationsByBreed("breed"));
  }

  @Test
  public void getVaccinationCountByBreed() {
    when(vaccinationRepository.findVaccinationsByPetIdIn(any())).thenReturn(testList);
    when(petService.queryPets(any())).thenReturn(Collections.singletonList(testPet));

    Integer result = vaccinationServiceImpl.getVaccinationCountByBreed("breed");
    assertEquals(Integer.valueOf(1), result);
  }

  @Test
  public void getVaccinationCountByBreedDBError() {
    when(petService.queryPets(any())).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.getVaccinationCountByBreed("breed"));
  }

  @Test
  public void deleteVaccination() {
    when(vaccinationRepository.existsById(anyLong())).thenReturn(true);
    vaccinationServiceImpl.deleteVaccination(1L);
    verify(vaccinationRepository).deleteById(any());
  }

  @Test
  public void deleteVaccinationBadID() {
    doThrow(new ResourceNotFound("Database unavailable")).when(vaccinationRepository)
        .deleteById(anyLong());
    assertThrows(ResourceNotFound.class,
        () -> vaccinationServiceImpl.deleteVaccination(1L));
  }

  @Test
  public void deleteVaccinationDBError() {
    doThrow(new ServiceUnavailable("Database unavailable")).when(vaccinationRepository)
        .existsById(anyLong());
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.deleteVaccination(1L));
  }

  @Test
  public void deleteVaccinationByUsername() {
    when(vaccinationRepository.existsByInnoculation(anyString())).thenReturn(true);
    vaccinationServiceImpl.deleteVaccinationByInnoculation("");
    verify(vaccinationRepository).deleteByInnoculation(any());
  }

  @Test
  public void deleteVaccinationByUsernameBadId() {
    doThrow(EmptyResultDataAccessException.class).when(vaccinationRepository)
        .existsByInnoculation(anyString());
    assertThrows(ServiceUnavailable.class,
        () -> vaccinationServiceImpl.deleteVaccinationByInnoculation(""));
  }

  @Test
  public void deleteVaccinationByUsernameDBError() {
    when(vaccinationRepository.existsByInnoculation(anyString())).thenReturn(false);
    assertThrows(ResourceNotFound.class,
        () -> vaccinationServiceImpl.deleteVaccinationByInnoculation(""));
  }
}
