package io.catalyte.training.entities;

import static io.catalyte.training.constants.StringConstants.REQUIRED_FIELD;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "name" + REQUIRED_FIELD)
  private String name;

  @NotBlank(message = "breed" + REQUIRED_FIELD)
  private String breed;

  private Integer age;

  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private final Set<Vaccination> Vaccinations = new HashSet<>();

  public Pet() {
  }

  public Pet(
      @NotBlank String name,
      @NotBlank String breed, Integer age) {
    this.name = name;
    this.breed = breed;
    this.age = age;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String type) {
    this.name = type;
  }

  public String getBreed() {
    return breed;
  }

  public void setBreed(String breed) {
    this.breed = breed;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Pet{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", make='"
        + breed
        + '\''
        + ", age="
        + age
        + '}';

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pet Pet = (Pet) o;
    return Objects.equals(id, Pet.id) &&
        Objects.equals(name, Pet.name) &&
        Objects.equals(breed, Pet.breed) &&
        Objects.equals(age, Pet.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, breed, age);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return Objects.isNull(id) &&
        Objects.isNull(name) &&
        Objects.isNull(breed) &&
        Objects.isNull(age);
  }
}
