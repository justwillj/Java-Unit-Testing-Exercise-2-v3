package io.catalyte.training.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.catalyte.training.entities.Pet;
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

}