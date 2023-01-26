package io.catalyte.training.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.catalyte.training.entities.Pet;
import io.catalyte.training.exceptions.ResourceNotFound;
import io.catalyte.training.exceptions.ServiceUnavailable;
import io.catalyte.training.repositories.PetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;

class PetServiceImplTest {

  @Mock
  PetRepository petRepository;

  @Mock
  VaccinationService vaccinationService;

  @InjectMocks
  PetServiceImpl petServiceImpl;

  Pet testPet;
  List<Pet> testList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testPet = new Pet("Zoey","Dog",1);
    testPet.setId(1L);

    testList.add(testPet);

    when(petRepository.findAll()).thenReturn(testList);
    when(petRepository.findAll(any(Example.class))).thenReturn(testList);
    when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(testList.get(0)));
    when(petRepository.save(any(Pet.class))).thenReturn(testList.get(0));
    when(petRepository.saveAll(anyCollection())).thenReturn(testList);

  }

  @Test
  void queryAllPets() {
    List<Pet> results = petServiceImpl.queryPets(new Pet());
    assertEquals(testList,results);
  }

  @Test
  void queryAllPetsWithSample() {
    List<Pet> results = petServiceImpl.queryPets(testPet);
    assertEquals(testList,results);
  }

  @Test
  void queryAllPetsDBError() {
    when(petRepository.findAll()).thenThrow(EmptyResultDataAccessException.class);

    assertThrows(ServiceUnavailable.class,
        () -> petServiceImpl.queryPets(new Pet()));

  }

  @Test
  public void getPet() {
    Pet result = petServiceImpl.getPet(1L);
    assertEquals(testPet, result);
  }

  @Test
  public void getPetDBError() {
    when(petRepository.findById(anyLong())).thenThrow(EmptyResultDataAccessException.class);
    assertThrows(ServiceUnavailable.class,
        () -> petServiceImpl.getPet(1L));
  }

  @Test
  public void getPetNotFound() {
    when(petRepository.findById(anyLong())).thenReturn(Optional.empty());
    Exception exception = assertThrows(ResourceNotFound.class,
        () -> petServiceImpl.getPet(1L));
    String expectedMessage = "Could not locate a Pet with the id: 1";
    assertEquals(expectedMessage,
        exception.getMessage(),
        () -> "Message did not equal '" + expectedMessage + "', actual message:"
            + exception.getMessage());
  }

  @Test
  public void addPet() {
    Pet result = petServiceImpl.addPet(testPet);
    assertEquals(testPet, result);
  }

  @Test
  public void addPetDBError() {
    when(petRepository.save(any(Pet.class))).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));
    assertThrows(ServiceUnavailable.class,
        () -> petServiceImpl.addPet(testPet));
  }

  @Test
  public void addPets() {
    List<Pet> result = petServiceImpl.addPets(testList);
    assertEquals(testList, result);
  }

  @Test
  public void addPetsDBError() {
    when(petRepository.saveAll(anyCollection())).thenThrow(
        new EmptyResultDataAccessException("Database unavailable", 0));

    assertThrows(ServiceUnavailable.class,
        () -> petServiceImpl.addPets(testList));
  }


}