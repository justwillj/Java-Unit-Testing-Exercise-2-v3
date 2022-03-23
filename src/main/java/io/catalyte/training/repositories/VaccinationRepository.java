package io.catalyte.training.repositories;

import io.catalyte.training.entities.Vaccination;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

  List<Vaccination> findVaccinationsByPetIdIn(List<Long> ids);


  boolean existsByInnoculation(String innoculation);

  @Transactional
  void deleteByInnoculation(String innoculation);
}
