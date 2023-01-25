package io.catalyte.training.services;

import static org.junit.jupiter.api.Assertions.*;

import io.catalyte.training.repositories.PetRepository;
import io.catalyte.training.repositories.VaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class PetServiceImplTest {

  @Mock
  PetRepository petRepository;

  @Mock
  VaccinationService vaccinationService;

  @InjectMocks
  PetServiceImpl petServiceImpl;

  @BeforeEach
  void setUp() {
  }

  @Test
  void queryPets() {
  }
}