package io.catalyte.training.services;

import io.catalyte.training.entities.Pet;
import java.util.List;

public interface PetService {

  Pet getPet(Long id);

  List<Pet> queryPets(Pet Pet);

  Pet addPet(Pet Pet);

  List<Pet> addPets(List<Pet> Pets);

  Pet updatePetById(Long id, Pet Pet);

  void deletePet(Long id);
}
